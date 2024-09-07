package com.online.auction.service;

import com.online.auction.dto.AuthenticationRequestDTO;
import com.online.auction.dto.AuthenticationResponseDTO;
import com.online.auction.dto.UserDTO;
import com.online.auction.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Service interface for managing user-related operations.
 * <p>
 * This interface provides methods for user registration, authentication, password reset, and token management.
 * </p>
 */
public interface UserService {
    /**
     * Registers a new user with the provided user details.
     * <p>
     * This method accepts a {@link UserDTO} object containing the user's information and registers a new user in the system.
     * </p>
     *
     * @param userDto The {@link UserDTO} object containing the user details for registration.
     * @return A confirmation message or user ID upon successful registration.
     * @throws ServiceException If there is an error during registration or if the user cannot be registered.
     */
    String register(UserDTO userDto) throws ServiceException;

    /**
     * Authenticates a user with the provided credentials.
     * <p>
     * This method takes an {@link AuthenticationRequestDTO} object containing the user's credentials and returns an {@link AuthenticationResponseDTO} object
     * containing authentication tokens and user details upon successful authentication.
     * </p>
     *
     * @param authenticationRequest The {@link AuthenticationRequestDTO} object containing user credentials for authentication.
     * @return An {@link AuthenticationResponseDTO} object containing authentication tokens and user details.
     * @throws ServiceException If there is an error during authentication or if the credentials are invalid.
     */
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequest) throws ServiceException;

    /**
     * Refreshes the authentication token based on the provided request and response.
     * <p>
     * This method generates a new authentication token based on the provided request and response objects and returns the updated token information.
     * </p>
     *
     * @param httpServletRequest  The {@link HttpServletRequest} object containing the current request information.
     * @param httpServletResponse The {@link HttpServletResponse} object to write the new token information to.
     * @return An {@link AuthenticationResponseDTO} object containing the refreshed token information.
     * @throws IOException If there is an error writing the response or processing the request.
     */
    AuthenticationResponseDTO refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * Sends a password reset link to the specified email address.
     * <p>
     * This method generates a password reset link and sends it to the user's email address for password reset purposes.
     * </p>
     *
     * @param email The email address to which the password reset link is sent.
     * @return A confirmation message indicating that the password reset link has been sent.
     * @throws ServiceException If there is an error sending the password reset link or if the email address is not associated with any user.
     */
    String sendPasswordResetLink(String email) throws ServiceException;

    /**
     * Resets the user's password using the provided token and new password.
     * <p>
     * This method processes the password reset request using the provided token and new password to update the user's password.
     * </p>
     *
     * @param token       The password reset token provided by the user.
     * @param newPassword The new password to set for the user.
     * @return A confirmation message indicating that the password has been successfully reset.
     * @throws ServiceException If there is an error resetting the password or if the token is invalid.
     */
    String resetPassword(String token, String newPassword) throws ServiceException;

}
