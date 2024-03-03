package com.eventbooking.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testInvalidEmail() {
        AuthRequest authRequest = new AuthRequest("invalid-email", "password");
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(authRequest);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidPassword() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "123");
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(authRequest);
        assertEquals(1, violations.size());
    }

    @Test
    public void testValidAuthRequest() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password123");
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(authRequest);
        assertEquals(0, violations.size());
    }
}