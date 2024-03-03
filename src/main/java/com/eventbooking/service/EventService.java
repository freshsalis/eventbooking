package com.eventbooking.service;

import com.eventbooking.dto.EventRequestDto;
import com.eventbooking.dto.EventResponseDto;
import com.eventbooking.dto.SearchEventRequestDto;
import com.eventbooking.dto.TicketRequest;
import com.eventbooking.entity.Event;
import com.eventbooking.entity.EventTicket;
import com.eventbooking.entity.User;
import com.eventbooking.repository.EventRepository;
import com.eventbooking.repository.EventTicketRepository;
import com.eventbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class EventService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventTicketRepository ticketRepository;

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


    @Transactional
    public EventTicket bookEvent(String username, int eventId, TicketRequest requestDto) {
        User user = userRepository.findByEmail(username).orElse( null);

        Event event = eventRepository.findById(eventId).orElse(null);

        if (user != null && event != null && event.getAvailableAttendeesCount() >= requestDto.getAttendeesCount()){


            event.setAvailableAttendeesCount(event.getAvailableAttendeesCount() - requestDto.getAttendeesCount());
            eventRepository.save(event);

            EventTicket eventTicket = ticketRepository.findByEventIdAndUserId(eventId, user.getId()).orElse(null);

            if (eventTicket != null){
                eventTicket.setAttendeesCount(eventTicket.getAttendeesCount() + requestDto.getAttendeesCount());
                return ticketRepository.save(eventTicket);
            }else{
                EventTicket ticket = EventTicket.builder().event(event)
                        .user(user)
                        .attendeesCount(requestDto.getAttendeesCount())
                        .reservationDate(new Date())
                        .build();

                return ticketRepository.save(ticket);
            }

        }else{
            return null;
        }

    }

    public List<Event> getBookedEvents(String username) {
        User user = userRepository.findByEmail(username).orElse( null);


        if (username != null ){

            List<Event> events = ticketRepository.findBookedEventsByUserId(user.getId());

            return events;
        }else{
            return null;
        }

    }

    @Transactional
    public EventTicket cancelReservation(String username, int eventId) {
        User user = userRepository.findByEmail(username).orElse( null);

        Event event = eventRepository.findById(eventId).orElse(null);

        if (user != null && event != null ){
            EventTicket eventTicket = ticketRepository.findByEventIdAndUserId(eventId, user.getId()).orElse(null);

            if (eventTicket != null){
                event.setAvailableAttendeesCount(event.getAvailableAttendeesCount() + eventTicket.getAttendeesCount());
                eventRepository.save(event);
                ticketRepository.delete(eventTicket);
            }

            return eventTicket;
        }else{
            return null;
        }

    }


}
