package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable
public record TimeSlot(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) implements Serializable {
}