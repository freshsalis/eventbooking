package com.eventbooking.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testInvalidName() {
        UserRequest userRequest = new UserRequest("", "test@example.com", "password");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidEmail() {
        UserRequest userRequest = new UserRequest("John Doe", "invalid-email", "password");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidPassword() {
        UserRequest userRequest = new UserRequest("John Doe", "test@example.com", "123");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertEquals(1, violations.size());
    }

    @Test
    public void testValidUserRequest() {
        UserRequest userRequest = new UserRequest("John Doe", "test@example.com", "password123");
        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertEquals(0, violations.size());
    }
}