package fr.hoenheimsports.trainingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.hoenheimsports.trainingservice.models.Category;
import fr.hoenheimsports.trainingservice.models.Gender;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.Team}
 */
@Relation(collectionRelation = "teams")
public record TeamDto( Long id, Gender gender, Category category, int teamNumber, @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) CoachDto coach,
                       @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) Set<TrainingSessionDto> trainingSessions) implements Serializable {
}