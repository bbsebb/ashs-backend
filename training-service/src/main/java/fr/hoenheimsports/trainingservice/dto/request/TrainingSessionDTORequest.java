package fr.hoenheimsports.trainingservice.dto.request;

import jakarta.validation.Valid;

import java.io.Serializable;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.TrainingSession}
 */
public record TrainingSessionDTORequest(Long id, @Valid TimeSlotDTORequest timeSlot, @Valid HallDTORequest hall) implements Serializable {
}