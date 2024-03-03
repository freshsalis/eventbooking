package com.eventbooking.controller;

import com.eventbooking.dto.EventRequestDto;
import com.eventbooking.dto.EventResponseDto;
import com.eventbooking.dto.SearchEventRequestDto;
import com.eventbooking.dto.UserRequest;
import com.eventbooking.entity.Event;
import com.eventbooking.entity.User;
import com.eventbooking.service.EventService;
import com.eventbooking.service.JwtService;
import com.eventbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
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
        return ResponseEntity.ok(events);
    }

    @PostMapping("/{eventId}/tickets")
    public ResponseEntity<?> create(Authentication authentication,
                                    @PathVariable("eventId") int eventId,
                                    @RequestBody  EventRequestDto requestDto){

        return ResponseEntity.status(HttpStatus.CREATED).body("ok here");
    }


}
