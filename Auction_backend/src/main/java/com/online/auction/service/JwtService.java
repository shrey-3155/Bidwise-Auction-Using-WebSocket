package com.online.auction.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;


/**
 * Service interface for managing JSON Web Tokens (JWT) operations.
 * <p>
 * This interface provides methods for extracting information from JWTs, generating new tokens,
 * and validating existing tokens.
 * </p>
 */
public interface JwtService {
    /**
     * Extracts the username from a given JWT.
     * <p>
     * This method parses the token and retrieves the username (or subject) claim from it.
     * </p>
     *
     * @param token The JWT from which the username is to be extracted.
     * @return The username extracted from the token.
     */
    String extractUsername(String token);

    /**
     * Extracts a specific claim from a given JWT.
     * <p>
     * This method allows the extraction of any specific claim from the token by using a provided
     * claims resolver function.
     * </p>
     *
     * @param <T>            The type of the claim to be extracted.
     * @param token          The JWT from which the claim is to be extracted.
     * @param claimsResolver A function to resolve the desired claim from the JWT's claims.
     * @return The extracted claim value.
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Generates a new JWT for a given user.
     * <p>
     * This method creates a new token with the claims derived from the provided user details.
     * </p>
     *
     * @param userDetails The user details for which the token is to be generated.
     * @return The newly generated JWT.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Generates a new JWT with additional claims for a given user.
     * <p>
     * This method creates a new token that includes additional claims beyond the standard user details.
     * </p>
     *
     * @param extraClaims Additional claims to be included in the token.
     * @param userDetails The user details for which the token is to be generated.
     * @return The newly generated JWT.
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Generates a new refresh token for a given user.
     * <p>
     * This method creates a new refresh token for the provided user details.
     * </p>
     *
     * @param userDetails The user details for which the refresh token is to be generated.
     * @return The newly generated refresh token.
     */
    String generateRefreshToken(UserDetails userDetails);

    /**
     * Checks whether a given JWT is valid based on the provided user details.
     * <p>
     * This method verifies the token's validity by checking if it is correctly signed and if it matches
     * the provided user details.
     * </p>
     *
     * @param token       The JWT to be validated.
     * @param userDetails The user details to be matched against the token.
     * @return {@code true} if the token is valid; {@code false} otherwise.
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Checks whether a given JWT is expired.
     * <p>
     * This method determines if the token's expiration date has passed.
     * </p>
     *
     * @param token The JWT to be checked for expiration.
     * @return {@code true} if the token is expired; {@code false} otherwise.
     */
    boolean isTokenExpired(String token);
}
