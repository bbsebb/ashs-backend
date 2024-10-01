package fr.hoenheimsports.trainingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.TrainingSession}
 */
@Relation(collectionRelation = "trainingSessions")
public record TrainingSessionDto(@JsonIgnore Long id, TimeSlotDto timeSlot,@JsonIgnore HallDto hall) implements Serializable {
}