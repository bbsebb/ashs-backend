package fr.hoenheimsports.trainingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.hoenheimsports.trainingservice.models.Hall;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;

/**
 * DTO for {@link Hall}
 */
@Relation(collectionRelation = "halls")
public record HallDto(@JsonIgnore Long id, String name, AddressDto address) implements Serializable {
}