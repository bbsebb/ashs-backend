package fr.hoenheimsports.trainingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.hoenheimsports.trainingservice.models.Category;
import fr.hoenheimsports.trainingservice.models.Gender;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.Team}
 */
@Relation(collectionRelation = "teams")
public record TeamDto(@JsonIgnore Long id, Gender gender, Category category, int teamNumber, @JsonIgnore CoachDto coach,
                      @JsonIgnore Set<TrainingSessionDto> trainingSessions) implements Serializable {
}