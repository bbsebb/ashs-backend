package fr.hoenheimsports.trainingservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable
public record TimeSlot(
        @Column(nullable = false) DayOfWeek dayOfWeek,
        @Column(nullable = false) LocalTime startTime,
        @Column(nullable = false) LocalTime endTime) implements Serializable {
}