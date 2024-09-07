package com.online.auction.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static com.online.auction.constant.TestConstants.ACCESS_TOKEN;
import static com.online.auction.constant.TestConstants.ADMIN;
import static com.online.auction.constant.TestConstants.JWT_EXPIRATION;
import static com.online.auction.constant.TestConstants.JWT_EXPIRATION_VALUE;
import static com.online.auction.constant.TestConstants.REFRESH_EXPIRATION;
import static com.online.auction.constant.TestConstants.REFRESH_EXPIRATION_VALUE;
import static com.online.auction.constant.TestConstants.ROLE;
import static com.online.auction.constant.TestConstants.SECRET_KEY;
import static com.online.auction.constant.TestConstants.SECRET_KEY_VALUE;
import static com.online.auction.constant.TestConstants.TEST_EMAIL;
import static com.online.auction.constant.TestConstants.TOKEN_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {
    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private UserDetails userDetails;

    private String secretKey = SECRET_KEY_VALUE;
    private long jwtExpiration = JWT_EXPIRATION_VALUE;
    private long refreshExpiration = REFRESH_EXPIRATION_VALUE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, SECRET_KEY, secretKey);
        ReflectionTestUtils.setField(jwtService, JWT_EXPIRATION, jwtExpiration);
        ReflectionTestUtils.setField(jwtService, REFRESH_EXPIRATION, refreshExpiration);
    }

//    @Test
//    void extractUsernameTest() {
//        String username = jwtService.extractUsername(ACCESS_TOKEN);
//        assertEquals(TOKEN_EMAIL, username);
//    }

    @Test
    void generateTokenTest() {
        when(userDetails.getUsername()).thenReturn(TOKEN_EMAIL);
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void generateTokenWithExtraClaimsTest() {
        when(userDetails.getUsername()).thenReturn(TOKEN_EMAIL);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(ROLE, ADMIN);
        String token = jwtService.generateToken(extraClaims, userDetails);
        assertNotNull(token);
    }

    @Test
    void generateRefreshTokenTest() {
        when(userDetails.getUsername()).thenReturn(TEST_EMAIL);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        assertNotNull(refreshToken);
    }

//    @Test
//    void isTokenValidTest() {
//        when(userDetails.getUsername()).thenReturn(TOKEN_EMAIL);
//        boolean isValid = jwtService.isTokenValid(ACCESS_TOKEN, userDetails);
//        assertTrue(isValid);
//    }
}