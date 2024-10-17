package fr.hoenheimsports.trainingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.Coach}
 */
@Relation(collectionRelation = "coaches")
public record CoachDto(Long id, String name, String surname, String email, String phone) implements Serializable {
}