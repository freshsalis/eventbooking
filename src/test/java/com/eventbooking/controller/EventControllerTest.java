package com.eventbooking.controller;

import com.eventbooking.dto.EventRequestDto;
import com.eventbooking.dto.EventResponseDto;
import com.eventbooking.dto.SearchEventRequestDto;
import com.eventbooking.dto.UserRequest;
import com.eventbooking.entity.Event;
import com.eventbooking.entity.User;
import com.eventbooking.filter.JwtAuthFilter;
import com.eventbooking.service.CustomUserDetailsService;
import com.eventbooking.service.EventService;
import com.eventbooking.service.JwtService;
import com.eventbooking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean
    private EventService eventService;

    @MockBean private JwtService jwtService;

    private EventController eventController;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username="test1@example.com")
    void createEventTest() throws Exception {
        EventRequestDto eventRequestDto = EventRequestDto.builder()
                .name("Event 1")
                .date(new Date())
                .availableAttendeesCount(50)
                .category(Event.Category.CONFERENCE)
                .description("Test description")
                .build();


        Event savedEvent = Event.builder()
                .name(eventRequestDto.getName())
                .date(eventRequestDto.getDate())
                .availableAttendeesCount(eventRequestDto.getAvailableAttendeesCount())
                .category(eventRequestDto.getCategory())
                .description(eventRequestDto.getDescription())
                .id(4)
                .build();
        EventResponseDto responseDto = new EventResponseDto();
        responseDto.setEventId(savedEvent.getId());


        when(eventService.create(any(EventRequestDto.class))).thenReturn(responseDto);

        String requestBody = objectMapper.writeValueAsString(eventRequestDto);


        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());
        ;

        verify(eventService).create(eventRequestDto);

    }

    @Test
    @WithMockUser(username="test1@example.com")
    void getEventsTest() throws Exception {
        SearchEventRequestDto requestDto = new SearchEventRequestDto();
        requestDto.setName("Test Event");
        requestDto.setStartDate(new Date(2023, 03, 04));
        requestDto.setStartDate(new Date(2023, 03, 05));
        requestDto.setCategory(Event.Category.CONCERT);

        List<Event> events = new ArrayList<>();
        Event event = new Event();
        event.setId(1);
        event.setName("Test Concert Event");
        event.setCategory(Event.Category.CONCERT);
        event.setDate(new Date(2023, 03, 04));
        event.setDescription("Test Description");
        event.setAvailableAttendeesCount(100);
        events.add(event);

        when(eventService.list(any(SearchEventRequestDto.class))).thenReturn(events);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(get("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());


        verify(eventService).list(requestDto);
    }

    @Test
    @WithMockUser(username="test1@example.com")
    void listEventReturnEmptyArray() throws Exception {
        SearchEventRequestDto requestDto = new SearchEventRequestDto();
        requestDto.setName("Test Event");
        requestDto.setStartDate(new Date(2023, 03, 04));
        requestDto.setStartDate(new Date(2023, 03, 05));
        requestDto.setCategory(Event.Category.CONCERT);

        List<Event> events = new ArrayList<>();


        when(eventService.list(any(SearchEventRequestDto.class))).thenReturn(events);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(get("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());


        verify(eventService).list(requestDto);
    }

}