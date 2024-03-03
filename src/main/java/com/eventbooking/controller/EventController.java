package com.eventbooking.controller;

import com.eventbooking.dto.EventRequestDto;
import com.eventbooking.dto.EventResponseDto;
import com.eventbooking.dto.SearchEventRequestDto;
import com.eventbooking.dto.TicketRequest;
import com.eventbooking.entity.Event;
import com.eventbooking.entity.EventTicket;
import com.eventbooking.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "events")
public class EventController {
    @Autowired
    EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDto> create(Authentication authentication, @RequestBody @Valid EventRequestDto requestDto){
        EventResponseDto responseDto = eventService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getEvents(Authentication authentication, @RequestBody @Valid SearchEventRequestDto requestDto){
        List<Event> events = eventService.list(requestDto);

        if (events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }else
            return ResponseEntity.ok(events);
    }

    @PostMapping("/{eventId}/tickets")
    public ResponseEntity<?> bookTicket(Authentication authentication,
                                    @PathVariable("eventId") int eventId,
                                    @RequestBody @Valid TicketRequest requestDto){
        String username = authentication.getName();
        EventTicket ticket = eventService.bookEvent(username, eventId, requestDto);
        if (ticket != null){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/booked-events")
    public ResponseEntity<?> bookedEvents(Authentication authentication){
        String username = authentication.getName();
        List<Event> events = eventService.getBookedEvents(username);
        if (!events.isEmpty()){
            return ResponseEntity.ok(events);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{eventId}/cancel-tickets")
    public ResponseEntity<?> cancelReservation(Authentication authentication,
                                          @PathVariable("eventId") int eventId){
        String username = authentication.getName();
        EventTicket ticket = eventService.cancelReservation(username, eventId);
        if (ticket != null){
            return ResponseEntity.status(HttpStatus.OK).build();
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
