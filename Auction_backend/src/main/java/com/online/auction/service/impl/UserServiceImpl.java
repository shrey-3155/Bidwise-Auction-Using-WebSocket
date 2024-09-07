package com.online.auction.service.impl;

import com.online.auction.dto.AuthenticationRequestDTO;
import com.online.auction.dto.AuthenticationResponseDTO;
import com.online.auction.dto.UserDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Account;
import com.online.auction.model.City;
import com.online.auction.model.Token;
import com.online.auction.model.TokenType;
import com.online.auction.model.User;
import com.online.auction.repository.AccountRepository;
import com.online.auction.repository.CityRepository;
import com.online.auction.repository.TokenRepository;
import com.online.auction.repository.UserRepository;
import com.online.auction.service.JwtService;
import com.online.auction.service.UserService;
import com.online.auction.utils.EmailUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.online.auction.constant.AuctionConstants.BEARER;
import static com.online.auction.constant.AuctionConstants.EMAIL_BODY_REGISTER;
import static com.online.auction.constant.AuctionConstants.EMAIL_SUBJECT;
import static com.online.auction.constant.AuctionConstants.INTEGER_SEVEN;
import static com.online.auction.constant.AuctionConstants.INVALID_CREDENTIALS_MSG;
import static com.online.auction.constant.AuctionConstants.PASSWORD_RESET_LINK;
import static com.online.auction.constant.AuctionConstants.PASSWORD_RESET_LINK_BODY;
import static com.online.auction.constant.AuctionConstants.PASSWORD_RESET_REQUEST;
import static com.online.auction.constant.AuctionConstants.USER_ALREADY_PRESENT_MSG;

/**
 * Service implementation for handling user-related operations.
 * This service provides methods for user registration, authentication, and password management.
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailUtils emailUtils;

    /**
     * The registration API for the User to register first time with the application
     *
     * @param userDto the user details for the registration
     * @return the successful message if the user registration is successful
     * @throws ServiceException when city as input is not present
     */
    @Override
    public String register(UserDTO userDto) throws ServiceException {
        log.info("User register call started in the UserServiceImpl");

        Optional<City> cityDb = cityRepository.findByCityName(userDto.getCity());
        if (cityDb.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "City requested is not present");
        }
        var user = com.online.auction.model.User.builder()
                .firstName(userDto.getFirstname())
                .lastName(userDto.getLastname())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(userDto.getRole())
                .city(cityDb.get())
                .timeZone(userDto.getTimezone())
                .isPremium(userDto.isPremium())
                .build();

        log.info("Checking if the user is present in the database");
        Optional<User> userEntryInDb = userRepository.findByEmail(userDto.getEmail());
        if (userEntryInDb.isPresent()) {
            log.error("The user is already present in the database");
            throw new UsernameNotFoundException(USER_ALREADY_PRESENT_MSG);
        }
        var userDb = userRepository.save(user);
        log.info("Successfully saved the user information in the database");
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(userDb, jwtToken);
        emailUtils.sendEmail(userDto.getEmail(), EMAIL_SUBJECT, EMAIL_BODY_REGISTER);

        // Create an initial account with zero balance for the new user
        createInitialAccount(userDb);

        return "User Registered Successfully";
    }

    /**
     * Creates an initial account with zero balance for a new user.
     *
     * @param user the user for whom the account is to be created
     */
    private void createInitialAccount(User user) {
        Account account = new Account();
        account.setUser(user);
        account.setFunds(0);
        accountRepository.save(account);
        log.info("Created initial account for user id: {} with initial funds: 0", user.getUserId());
    }

    /**
     * The authentication API for the user to get the access and refresh token
     * using the email and password
     *
     * @param authenticationRequestDTO UserEmail  and Password
     * @return the JWT access token and refresh token generated
     * @throws ServiceException when the credentials entered as invalid
     */
    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) throws ServiceException {
        log.info("User authenticate call started in the UserServiceImpl");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequestDTO.getEmail(),
                            authenticationRequestDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.error("Invalid Credentials Exception: {}", e.getMessage());
            throw new ServiceException(HttpStatus.FORBIDDEN, INVALID_CREDENTIALS_MSG);
        }

        var user = userRepository.findByEmail(authenticationRequestDTO.getEmail())
                .orElseThrow();
        log.info("Generating the Access token for the user");
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        log.info("Successfully completed the access token generation for the user:{}", user);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * The Refresh token API to get the new access token for the already logged-in user
     *
     * @param request             the HTTPServletRequest
     * @param httpServletResponse the HTTPServletResponse
     * @return the new access token generated from the refresh token
     * @throws IOException when IO error while generating the token
     */
    @Override
    public AuthenticationResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        log.info("Refresh token generation call started in the UserServiceImpl");
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            log.error("The Authentication header is NULL in the refresh token generation");
            return null;
        }

        refreshToken = authHeader.substring(INTEGER_SEVEN);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            log.info("The user found with the email Id: {}", userEmail);
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                log.info("Generating the new Access token with the Refresh token for the user: {}", user);
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                return authResponse;
            }
        }
        return null;
    }

    /**
     * This method saves the token in the token repository
     *
     * @param user     the user object
     * @param jwtToken the JWT access token to be stored
     */
    private void saveUserToken(User user, String jwtToken) {
        log.info("Saving the new Access token generated for the user : {}", user);
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * This method revokes all the older token present in the database
     *
     * @param user the user object
     */
    private void revokeAllUserTokens(User user) {
        log.info("Revoking all the older access token for the user :{}", user);
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * This method sends password reset link
     *
     * @param email takes user email to validate the email and send reset password link
     * @return a string
     */
    public String sendPasswordResetLink(String email) throws ServiceException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, "User not found"));
        user.setResetToken(UUID.randomUUID().toString());
        userRepository.save(user);
        emailUtils.sendEmail(email, PASSWORD_RESET_REQUEST, PASSWORD_RESET_LINK_BODY + PASSWORD_RESET_LINK + user.getResetToken());
        return "Password Reset Link Successfully";
    }

    /**
     * This method updates the user password
     *
     * @param token       resetToken which is used to validate that user has received reset password link
     * @param newPassword new user password string
     * @return a string
     */
    public String resetPassword(String token, String newPassword) throws ServiceException {
        User user = userRepository.findByResetToken(token).orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, "Invalid token"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
        return "Password Reset Successful";
    }
}
