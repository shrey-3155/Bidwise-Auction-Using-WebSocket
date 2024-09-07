package com.online.auction.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.auction.dto.CustomErrorResponseDTO;
import com.online.auction.repository.TokenRepository;
import com.online.auction.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

import static com.online.auction.constant.AuctionConstants.APPLICATION_JSON;
import static com.online.auction.constant.AuctionConstants.AUTHORIZATION;
import static com.online.auction.constant.AuctionConstants.BEARER;
import static com.online.auction.constant.AuctionConstants.INTEGER_SEVEN;
import static com.online.auction.constant.AuctionConstants.JWT_EXPIRED_MSG;
import static com.online.auction.constant.AuctionConstants.UNAUTHORIZED;
import static com.online.auction.constant.AuctionConstants.UNAUTHORIZED_STATUS_CODE;


/**
 * JWT Authentication Filter for handling and validating JWT tokens in HTTP requests.
 * <p>
 * This filter intercepts HTTP requests to check for the presence of a JWT token in the Authorization header,
 * validates the token, and sets the authentication in the Spring Security context if the token is valid.
 * It also handles expired JWT tokens by returning an appropriate error response.
 * </p>
 *
 * <p>
 * The filter processes the following scenarios:
 * <ul>
 *     <li>Skips filtering for paths containing "/api/v1/auth".</li>
 *     <li>Extracts and validates the JWT token from the Authorization header.</li>
 *     <li>Sets the user authentication if the token is valid and not expired.</li>
 *     <li>Handles expired JWT tokens by sending a 401 Unauthorized response with an error message.</li>
 * </ul>
 * </p>
 *
 * @see JwtService
 * @see UserDetailsService
 * @see TokenRepository
 * @see CustomErrorResponseDTO
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    /**
     * Filters HTTP requests to extract and validate JWT tokens.
     * <p>
     * This method is called once per request to filter and validate the JWT token from the Authorization header.
     * If the token is valid, the user authentication is set in the Spring Security context.
     * If the token is expired, a 401 Unauthorized error response is returned.
     * </p>
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain to pass the request and response.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException      If an error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(INTEGER_SEVEN);
        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            handleExpiredJwtException(response);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Handles expired JWT tokens by sending a 401 Unauthorized error response.
     * <p>
     * This method is invoked when an expired JWT token is detected. It sets the response status to 401 and writes
     * a JSON error message to the response body.
     * </p>
     *
     * @param response The HTTP response to which the error message is written.
     * @throws IOException If an error occurs while writing the response.
     */
    private void handleExpiredJwtException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON);
        String jsonResponse = new ObjectMapper().writeValueAsString(new CustomErrorResponseDTO(
                new Date().toString(),
                UNAUTHORIZED_STATUS_CODE,
                UNAUTHORIZED,
                JWT_EXPIRED_MSG
        ));
        response.getWriter().write(jsonResponse);
    }
}
