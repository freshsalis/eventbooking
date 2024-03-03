package com.eventbooking.controller;

import com.eventbooking.dto.AuthRequest;
import com.eventbooking.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(jwtService.generateToken(authRequest.getEmail())).thenReturn("token");

        ResponseEntity<String> response = authController.login(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", response.getBody());
    }

    @Test
    public void testLogin_AuthenticationException() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");

        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("Invalid credentials") {});

        ResponseEntity<String> response = authController.login(authRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
