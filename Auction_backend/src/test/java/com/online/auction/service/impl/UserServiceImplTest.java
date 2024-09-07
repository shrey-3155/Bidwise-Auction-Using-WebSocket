package com.online.auction.service.impl;

import com.online.auction.dto.AuthenticationRequestDTO;
import com.online.auction.dto.AuthenticationResponseDTO;
import com.online.auction.dto.UserDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.City;
import com.online.auction.model.Token;
import com.online.auction.model.TokenType;
import com.online.auction.model.User;
import com.online.auction.repository.AccountRepository;
import com.online.auction.repository.CityRepository;
import com.online.auction.repository.TokenRepository;
import com.online.auction.repository.UserRepository;
import com.online.auction.service.JwtService;
import com.online.auction.utils.EmailUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.online.auction.constant.TestConstants.BAD_CREDENTIALS;
import static com.online.auction.constant.TestConstants.BEARER;
import static com.online.auction.constant.TestConstants.CITY_HALIFAX;
import static com.online.auction.constant.TestConstants.INTEGER_ONE;
import static com.online.auction.constant.TestConstants.JWT_TOKEN;
import static com.online.auction.constant.TestConstants.NEW_ACCESS_TOKEN;
import static com.online.auction.constant.TestConstants.PASSWORD;
import static com.online.auction.constant.TestConstants.REFRESH_TOKEN;
import static com.online.auction.constant.TestConstants.TEST_EMAIL;
import static com.online.auction.constant.TestConstants.USER_REGISTRATION_SUCCESS_MSG;
import static com.online.auction.constant.TestConstants.VALID_REFRESH_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private EmailUtils emailUtils;

    private UserDTO userDto;
    private User user;
    private String jwtToken = JWT_TOKEN;
    private String refreshToken = REFRESH_TOKEN;

    @BeforeEach
    void setUp() {
        userDto = new UserDTO();
        userDto.setEmail(TEST_EMAIL);
        userDto.setPassword(PASSWORD);

        user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        City city = getCity();
        userDto.setCity(CITY_HALIFAX);
        user.setCity(city);
    }

    @Test
    @SneakyThrows
    void registerSuccessfulTest() {
        City city = getCity();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(cityRepository.findByCityName(anyString())).thenReturn(Optional.of(city));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(refreshToken);
        doNothing().when(emailUtils).sendEmail(anyString(), anyString(), anyString());
        String response = userService.register(userDto);
        assertNotNull(response);
        assertEquals(response, USER_REGISTRATION_SUCCESS_MSG);
    }

    @Test
    void registerAlreadyExistingUserTest() {
        City city = getCity();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(cityRepository.findByCityName(anyString())).thenReturn(Optional.of(city));
        assertThrows(UsernameNotFoundException.class, () -> userService.register(userDto));
        verify(userRepository).findByEmail(TEST_EMAIL);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUserFailureWhenCityIsNotPresent() {
        assertThrows(ServiceException.class, () -> userService.register(userDto));
    }

    @SneakyThrows
    @Test
    void authenticateSuccessTest() {
        AuthenticationRequestDTO authenticationRequest = new AuthenticationRequestDTO();
        authenticationRequest.setEmail(TEST_EMAIL);
        authenticationRequest.setPassword(PASSWORD);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(refreshToken);

        Token token = new Token(INTEGER_ONE, jwtToken, TokenType.BEARER, false, false, user);
        when(tokenRepository.findAllValidTokenByUser(user.getUserId())).thenReturn(List.of(token));
        AuthenticationResponseDTO authenticationResponse = userService.authenticate(authenticationRequest);
        assertNotNull(authenticationResponse);
        assertEquals(jwtToken, authenticationResponse.getAccessToken());
        assertEquals(refreshToken, authenticationResponse.getRefreshToken());
    }

    @SneakyThrows
    @Test
    void authenticateFailureOnBadCredentialsTest() {
        AuthenticationRequestDTO authenticationRequest = new AuthenticationRequestDTO();
        authenticationRequest.setEmail(TEST_EMAIL);
        authenticationRequest.setPassword(PASSWORD);
        doThrow(new BadCredentialsException(BAD_CREDENTIALS)).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        ServiceException exception = assertThrows(ServiceException.class, () -> userService.authenticate(authenticationRequest));
    }

    @SneakyThrows
    @Test
    void refreshTokenTest() {
        String refreshToken = VALID_REFRESH_TOKEN;
        String email = TEST_EMAIL;
        String accessToken = NEW_ACCESS_TOKEN;
        User user = new User();
        user.setEmail(email);
        user.setUserId(1);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(BEARER + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(accessToken);


        AuthenticationResponseDTO result = userService.refreshToken(request, response);

        verify(jwtService, times(1)).generateToken(user);

        AuthenticationResponseDTO expectedResponse = AuthenticationResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        assertEquals(result, expectedResponse);
    }

    @SneakyThrows
    @Test
    void refreshTokenFailureWhenNullAuthHeaderTest() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setUserId(INTEGER_ONE);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        AuthenticationResponseDTO result = userService.refreshToken(request, response);
        assertNull(result);
    }

    @SneakyThrows
    @Test
    void refreshToken_when_user_is_null() {
        User user = new User();
        user.setEmail(TEST_EMAIL);
        user.setUserId(INTEGER_ONE);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(BEARER + VALID_REFRESH_TOKEN);
        when(jwtService.extractUsername(VALID_REFRESH_TOKEN)).thenReturn(null);

        AuthenticationResponseDTO result = userService.refreshToken(request, response);
        assertNull(result);
    }

    private City getCity() {
        City city = new City();
        city.setCityId(INTEGER_ONE);
        city.setCityName(CITY_HALIFAX);
        return city;
    }
}