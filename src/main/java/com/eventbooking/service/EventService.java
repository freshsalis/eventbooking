package com.eventbooking.service;

import com.eventbooking.dto.EventRequestDto;
import com.eventbooking.dto.EventResponseDto;
import com.eventbooking.dto.SearchEventRequestDto;
import com.eventbooking.dto.UserRequest;
import com.eventbooking.entity.Event;
import com.eventbooking.entity.User;
import com.eventbooking.repository.EventRepository;
import com.eventbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class EventService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    public EventResponseDto create(EventRequestDto request){

        Event eventBuilder = Event.builder()
                .name(request.getName())
                .category(request.getCategory())
                .date(request.getDate())
                .description(request.getDescription())
                .availableAttendeesCount(request.getAvailableAttendeesCount())
                .build();
        Event createdEvent = eventRepository.save(eventBuilder);

        return EventResponseDto.builder().eventId(createdEvent.getId()).build();
    }

    public List<Event> list(SearchEventRequestDto request){

        List<Event> events;
        if (!StringUtils.isEmpty(request.getName()) ||
                !StringUtils.isEmpty(request.getStartDate()) ||
                !StringUtils.isEmpty(request.getEndDate()) || request.getCategory() != null) {
            events = eventRepository.findByNameContainingIgnoreCaseOrDateBetweenOrCategory(
                    request.getName(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getCategory()
            );
        } else {
            events = eventRepository.findAll();
        }

        return events;
    }


}
