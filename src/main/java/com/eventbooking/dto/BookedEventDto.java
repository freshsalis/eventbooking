package com.eventbooking.dto;

import com.eventbooking.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookedEventDto {
    private Event event;
    private int attendanceCount;
}
