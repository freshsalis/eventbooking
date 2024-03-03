package com.eventbooking.controller;

import com.eventbooking.dto.EventRequestDto;
import com.eventbooking.dto.EventResponseDto;
import com.eventbooking.dto.SearchEventRequestDto;
import com.eventbooking.dto.TicketRequest;
import com.eventbooking.entity.Event;
import com.eventbooking.entity.EventTicket;
import com.eventbooking.repository.UserRepository;
import com.eventbooking.service.CustomUserDetailsService;
import com.eventbooking.service.EventService;
import com.eventbooking.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    private UserRepository userRepository;



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
    void testListEventReturnEmptyArray() throws Exception {
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
                .andExpect(status().isNotFound())
                .andDo(print());


        verify(eventService).list(requestDto);
    }

    @Test
    @WithMockUser(username="test1@example.com")
    void bookTicketTest() throws Exception {
        TicketRequest ticketRequest = TicketRequest.builder()
                .attendeesCount(5)
                .build();

        Event event = Event.builder()
                .name("Test Event")
                .date(new Date())
                .availableAttendeesCount(100)
                .category(Event.Category.CONCERT)
                .description("Test description")
                .id(4)
                .build();

        EventTicket eventTicket = EventTicket.builder()
                .attendeesCount(ticketRequest.getAttendeesCount())
                .event(event)
                .id(1)
                .build();

        when(eventService.bookEvent("test1@example.com", event.getId(), ticketRequest)).thenReturn(eventTicket);

        String requestBody = objectMapper.writeValueAsString(ticketRequest);

        mockMvc.perform(post("/events/4/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());

        verify(eventService).bookEvent("test1@example.com", event.getId(), ticketRequest);
    }

    @Test
    @WithMockUser(username="test1@example.com")
    void bookTicketShouldReturn400IfAttendeeCountGreaterThanAvailableAttendanceCountTest() throws Exception {
        TicketRequest ticketRequest = TicketRequest.builder()
                .attendeesCount(10000)
                .build();

        Event event = Event.builder()
                .name("Test Event")
                .date(new Date())
                .availableAttendeesCount(100)
                .category(Event.Category.CONCERT)
                .description("Test description")
                .id(4)
                .build();

        when(eventService.bookEvent("test1@example.com", event.getId(), ticketRequest))
                .thenReturn(null); // Return null to simulate the scenario where the attendees count is invalid

        String requestBody = objectMapper.writeValueAsString(ticketRequest);

        mockMvc.perform(post("/events/4/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(eventService).bookEvent("test1@example.com", event.getId(), ticketRequest);
    }

    @Test
    @WithMockUser(username="test1@example.com")
    void bookedEventsTest() throws Exception {

        List<Event> events = new ArrayList<>();
        Event event = new Event();
        event.setId(1);
        event.setName("Test Concert Event");
        event.setCategory(Event.Category.CONCERT);
        event.setDate(new Date(2023, 03, 04));
        event.setDescription("Test Description");
        event.setAvailableAttendeesCount(100);
        events.add(event);

        when(eventService.getBookedEvents(any())).thenReturn(events);


        mockMvc.perform(get("/events/booked-events")
                        .contentType(MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isOk())
                .andDo(print());


        verify(eventService).getBookedEvents("test1@example.com");
    }

    @Test
    @WithMockUser(username="test1@example.com")
    void bookedEventsShouldReturn404Test() throws Exception {

        List<Event> events = new ArrayList<>();

        when(eventService.getBookedEvents(any())).thenReturn(events);


        mockMvc.perform(get("/events/booked-events")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());


        verify(eventService).getBookedEvents("test1@example.com");
    }

    @Test
    @WithMockUser(username="test1@example.com")
    void cancelReservationTest() throws Exception {

        List<Event> events = new ArrayList<>();

        when(eventService.getBookedEvents(any())).thenReturn(events);

        mockMvc.perform(get("/events/booked-events")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(eventService).getBookedEvents("test1@example.com");
    }

    @Test
    @WithMockUser(username = "test1@example.com")
    public void cancelReservationShouldReturnOkWhenTicketIsCanceled() throws Exception {

        when(eventService.cancelReservation("test1@example.com", 1)).thenReturn(new EventTicket());

        mockMvc.perform(delete("/events/1/cancel-tickets"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(username = "test1@example.com")
    public void cancelReservationShouldReturnBadRequestWhenTicketIsNotCanceled() throws Exception {

        when(eventService.cancelReservation("test1@example.com", 1)).thenReturn(null);

        mockMvc.perform(delete("/events/1/cancel-tickets"))
                .andExpect(status().isBadRequest()).andDo(print());
    }




}