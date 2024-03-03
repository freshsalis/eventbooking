package com.eventbooking.repository;

import com.eventbooking.entity.Event;
import com.eventbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByNameContainingIgnoreCaseOrDateBetweenOrCategory(String name, Date startDate, Date endDate, Event.Category categories);


}
