package com.online.auction.service.impl;

import com.online.auction.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service implementation for handling Access token related operations.
 */
@Service
public class JwtServiceImpl implements JwtService {

    // Secret key used for signing the JWT, injected from application properties
    @Value("${application.security.jwt.secret-key:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String secretKey;

    // JWT expiration time in milliseconds, injected from application properties
    @Value("${application.security.jwt.expiration:86400000}")
    private long jwtExpiration;

    // Refresh token expiration time in milliseconds, injected from application properties
    @Value("${application.security.jwt.refresh-token.expiration:604800000}")
    private long refreshExpiration;


    /**
     * Extracts the username from the given JWT token
     *
     * @param token the access token
     * @return the user emailId
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * Extracts a specific claim from the JWT token using the provided claims resolver function
     *
     * @param token          the access token
     * @param claimsResolver the claim type required
     * @param <T>            return type of the claim
     * @return the specified claim
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    /**
     * Generates a JWT token for the given user details
     *
     * @param userDetails the user object
     * @return the JWT access token
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }


    /**
     * Generates a JWT token with extra claims for the given user details
     *
     * @param extraClaims additional claims key-value pair
     * @param userDetails the user object
     * @return the JWT access token with the additional claims
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }


    /**
     * Generates a refresh token for the given user details
     *
     * @param userDetails the user object
     * @return the JWT refresh token
     */
    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }


    /**
     * Checks if the given JWT token is valid for the given user details
     *
     * @param token       the access token
     * @param userDetails the user object
     * @return True if the token is valid. False otherwise.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    /**
     * Checks if the given JWT token is expired
     *
     * @param token the access token
     * @return True if the token is expired. False otherwise.
     */
    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /**
     * Extracts the expiration date from the given JWT token
     *
     * @param token the access token
     * @return the expiration time of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    /**
     * Builds a JWT token with the provided claims, user details, and expiration time
     *
     * @param extraClaims the claims key-value pair
     * @param userDetails the user object
     * @param expiration  the expiration time for the token
     * @return the JWT access token
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * Extracts all claims from the given JWT token
     *
     * @param token the access token
     * @return the claims in the JWT token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    /**
     * Retrieves the signing key for the JWT by decoding the secret key
     *
     * @return the signing key for the token generation
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
