package com.eventbooking.service;

import com.eventbooking.entity.Event;
import com.eventbooking.entity.EventTicket;
import com.eventbooking.entity.User;
import com.eventbooking.repository.EventRepository;
import com.eventbooking.repository.EventTicketRepository;
import com.eventbooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventNotificationServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventTicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Logger logger;

    @InjectMocks
    private EventNotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendNotificationsForUpcomingEvents() {
        // Arrange
        LocalDate today = LocalDate.now();

        LocalDate tomorrow = today.plusDays(1);

        // SEND NOTIFICATION A DAY BEFORE
        Date date = Date.from(tomorrow.atStartOfDay(ZoneId.of("UTC")).toInstant());


        Event event = new Event();
        event.setId(1);
        event.setName("Test Event");
        event.setDate(date);

        User user = new User();
        user.setId(1);
        user.setEmail("test1@email.com");

        EventTicket ticket = new EventTicket();
        ticket.setId(1);
        ticket.setUser(user);
        ticket.setEvent(event);

        List<Event> events = new ArrayList<>();
        events.add(event);

        List<EventTicket> tickets = new ArrayList<>();
        tickets.add(ticket);

        when(eventRepository.findUpcomingEvents(date)).thenReturn(events);
        when(ticketRepository.findByEventId(1)).thenReturn(tickets);
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(user));

        // Act
        notificationService.sendNotificationsForUpcomingEvents();

        String message = "Upcoming Event: " + event.getName().concat("\nDate: "+event.getDate());
        logger.info("Upcoming event email notification sent to {}, message: {} sent on {}",
                user.getEmail(), message, date);

    }

}