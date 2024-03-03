package com.eventbooking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.eventbooking.dto.UserRequest;
import com.eventbooking.entity.User;
import com.eventbooking.filter.JwtAuthFilter;
import com.eventbooking.service.CustomUserDetailsService;
import com.eventbooking.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    private static final String END_POINT_PATH = "/users";

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private UserService service;

    @MockBean private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testCreateShouldReturn201Created() throws Exception {
        UserRequest userRequest = UserRequest.builder()
                .email("test4@email.com")
                .name("test4")
                .password("password")
                .build();


        User savedUser = User.builder()
                .id(1)
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        Mockito.when(service.create(any(UserRequest.class))).thenReturn(savedUser);

        String requestBody = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());

        Mockito.verify(service, times(1)).create(userRequest);
    }

    @Test
    public void testCreateShouldReturn400BadRequest() throws Exception {
        UserRequest newUser = new UserRequest();
        newUser.setEmail("");
        newUser.setName("");
        newUser.setPassword("");

        String requestBody = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    public void testCreateShouldReturn400ForInvalidEmail() throws Exception {
        UserRequest newUser = new UserRequest();
        newUser.setEmail("");
        newUser.setName("Salisu Bako");
        newUser.setPassword("12345678kjjkj");

        String requestBody = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }
}
