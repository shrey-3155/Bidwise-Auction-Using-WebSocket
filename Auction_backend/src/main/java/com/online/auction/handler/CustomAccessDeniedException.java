package com.online.auction.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * Custom implementation of {@link AccessDeniedHandler} for handling access denial errors.
 * <p>
 * This class is used to handle situations where a user tries to access a resource they do not have permission for.
 * It customizes the response sent to the client when access is denied, providing a JSON response with an error message.
 * </p>
 *
 * @see AccessDeniedHandler
 */
public class CustomAccessDeniedException implements AccessDeniedHandler {

    /**
     * Handles access denied scenarios by sending a custom JSON error response.
     * <p>
     * This method is called when a user tries to access a resource they are not authorized to access.
     * It sets the HTTP response status to 403 Forbidden and writes a JSON error message to the response body.
     * </p>
     *
     * @param request               The HTTP request that resulted in an access denied error.
     * @param response              The HTTP response to which the error message is written.
     * @param accessDeniedException The exception that caused the access denial.
     * @throws IOException      If an error occurs while writing the response.
     * @throws ServletException If a servlet-specific error occurs.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Access denied\", \"reason\": \"" + "The user is not Authorized" + "\"}");
        response.getWriter().flush();
    }
}
