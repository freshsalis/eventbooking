package com.eventbooking.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserDetails userDetails;


    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
    }

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MUBlbWFpbC5jb20iLCJpYXQiOjE3MDkzODg3ODYsImV4cCI6MzA3Njg5OTgxNjM1ODgwMH0.qshQot70kQ8kGAaEGz6-FeZMdXuHF_r6GgmREV2Qh_o";

    @Test
    public void testExtractUsername() {
        String username = jwtService.extractUsername(TOKEN);
        assertEquals("test1@email.com", username);
    }

    @Test
    public void testExtractExpiration() {
        Date expiration = jwtService.extractExpiration(TOKEN);
        assertNotNull(expiration);
    }

    @Test
    public void testIsTokenExpired() {
        assertFalse(jwtService.isTokenExpired(TOKEN));
    }

    @Test
    public void testValidateToken() {
        String username = "test1@email.com";
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertTrue(jwtService.validateToken(TOKEN, userDetails));
    }

    @Test
    public void testGenerateToken() {
        String username = "test1@email.com";

        String token = jwtService.generateToken(username);

        assertNotNull(token);

        Key signingKey = jwtService.getSignKey();
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(username, claims.getSubject());
    }

}