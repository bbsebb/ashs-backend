package fr.hoenheimsports.trainingservice.dto;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.TimeSlot}
 */
public record TimeSlotDto(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) implements Serializable {
}