package com.eventbooking.service;

import com.eventbooking.entity.Event;
import com.eventbooking.entity.EventTicket;
import com.eventbooking.entity.User;
import com.eventbooking.repository.EventRepository;
import com.eventbooking.repository.EventTicketRepository;
import com.eventbooking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Service
public class EventNotificationService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventTicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventNotificationService.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:sS", Locale.ENGLISH);

    @Scheduled(cron = "0 0 8 * * *") // Runs every day at 8 AM
    public void sendNotificationsForUpcomingEvents() {

        LocalDate today = LocalDate.now();

        LocalDate tomorrow = today.plusDays(1);

        // SEND NOTIFICATION A DAY BEFORE
        Date date = Date.from(tomorrow.atStartOfDay(ZoneId.of("UTC")).toInstant());

        List<Event> upcomingEvents = eventRepository.findUpcomingEvents(date);

        for (Event event : upcomingEvents) {
            List<EventTicket> eventTickets = ticketRepository.findByEventId(event.getId());
            for (EventTicket ticket : eventTickets) {
                User user = userRepository.findById(ticket.getUser().getId()).orElse(null);
                if (user != null){
                     // SEND NOTIFICATION
                    String message = "Upcoming Event: " + event.getName().concat("\nDate: "+event.getDate());
                    logger.info("Upcoming event email notification sent to {}, message: {} sent on {}",
                            user.getEmail(), message, DATE_FORMAT.format(date));
                }
            }
        }
    }
}
