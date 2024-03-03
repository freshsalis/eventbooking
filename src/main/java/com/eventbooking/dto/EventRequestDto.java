package com.eventbooking.dto;

import com.eventbooking.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {

    @NotBlank
    @Size( max = 100)
    @NotBlank
    private String name;

    @NotNull
    private Date date;

    @NotNull
    @Max(100)
    @Min(1)
    private int availableAttendeesCount;

    @NotNull
    @NotBlank
    @Size(min=1, max = 500)
    private String description;

    @NotNull
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Event.Category category;

}
