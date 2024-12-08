package fr.hoenheimsports.trainingservice.dto.request;

import fr.hoenheimsports.trainingservice.validators.OnCreate;
import fr.hoenheimsports.trainingservice.validators.OnUpdate;
import fr.hoenheimsports.trainingservice.validators.annotations.StartBeforeEnd;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.TimeSlot}
 */
@StartBeforeEnd(groups = {OnCreate.class, OnUpdate.class})
public record TimeSlotDTORequest(
        @NotNull(message = "Le jour du créneau ne peut pas être vide", groups = {OnCreate.class}) DayOfWeek dayOfWeek,
        @NotNull(message = "La date de début du créneau ne peut pas être vide", groups = {OnCreate.class}) LocalTime startTime,
        @NotNull(message = "La date de fin du créneau ne peut pas être vide", groups = {OnCreate.class}) LocalTime endTime) implements Serializable {
}