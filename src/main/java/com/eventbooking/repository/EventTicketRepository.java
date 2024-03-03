package com.eventbooking.repository;

import com.eventbooking.entity.Event;
import com.eventbooking.entity.EventTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventTicketRepository extends JpaRepository<EventTicket, Integer> {
    @Query("SELECT et.event FROM EventTicket et JOIN et.user u WHERE u.id = :userId")
    List<Event> findBookedEventsByUserId(@Param("userId") int userId);

    Optional<EventTicket> findByEventIdAndUserId(int eventId, int id);
}
