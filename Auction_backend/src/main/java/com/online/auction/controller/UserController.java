package com.online.auction.controller;

import com.online.auction.dto.AuthenticationRequestDTO;
import com.online.auction.dto.AuthenticationResponseDTO;
import com.online.auction.dto.ResetEmailDTO;
import com.online.auction.dto.ResetTokenAndPasswordDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.dto.UserDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.USER;

/**
 * Controller for managing user-related operations, including registration, authentication, and password management.
 */
@RestController
@RequestMapping(API_VERSION_V1 + USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Registers a new user with the provided user details.
     *
     * @param userDto The details of the user to register.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} with the result of the registration operation.
     * @throws ServiceException If there is an error during registration or if the user cannot be registered.
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<String>> register(@RequestBody UserDTO userDto) throws ServiceException {
        String authenticationResponse = userService.register(userDto);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, authenticationResponse);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param authenticationRequest The request containing user credentials for authentication.
     * @return A {@link ResponseEntity} containing an {@link AuthenticationResponseDTO} with authentication details.
     * @throws ServiceException If there is an error during authentication or if the credentials are invalid.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO authenticationRequest
    ) throws ServiceException {
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }

    /**
     * Refreshes the authentication token based on the provided request and response.
     *
     * @param request  The HTTP servlet request containing the old token.
     * @param response The HTTP servlet response to write the new token.
     * @return A {@link ResponseEntity} containing an {@link AuthenticationResponseDTO} with the new token.
     * @throws IOException If there is an error during token refresh.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return ResponseEntity.ok(userService.refreshToken(request, response));
    }

    /**
     * Sends a password reset link to the provided email address.
     *
     * @param emailDTO The email address for which the password reset link is to be sent.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} with the result of the password reset link operation.
     * @throws ServiceException If there is an error sending the password reset link or if the email address is not found.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<SuccessResponse<String>> forgotPassword(@RequestBody ResetEmailDTO emailDTO) throws ServiceException {
        String email = emailDTO.getEmail();
        String resetLinkResponse = userService.sendPasswordResetLink(email);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, resetLinkResponse);
        return ResponseEntity.ok(response);
    }

    /**
     * Resets the user password based on the provided reset token and new password.
     *
     * @param resetDTO The reset token and new password to set.
     * @return A {@link ResponseEntity} containing a {@link SuccessResponse} with the result of the password reset operation.
     * @throws ServiceException If there is an error resetting the password or if the token is invalid.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse<String>> resetPassword(@RequestBody ResetTokenAndPasswordDTO resetDTO) throws ServiceException {
        String token = resetDTO.getToken();
        String newPassword = resetDTO.getNewPassword();
        String passwordUpdateResponse = userService.resetPassword(token, newPassword);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, passwordUpdateResponse);
        return ResponseEntity.ok(response);
    }

}