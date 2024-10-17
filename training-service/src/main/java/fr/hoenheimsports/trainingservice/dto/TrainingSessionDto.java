package fr.hoenheimsports.trainingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.TrainingSession}
 */
@Relation(collectionRelation = "trainingSessions")
public record TrainingSessionDto(Long id, TimeSlotDto timeSlot, HallDto hall) implements Serializable {
}