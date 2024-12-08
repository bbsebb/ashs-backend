package fr.hoenheimsports.trainingservice.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "coaches")
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class CoachDTO extends RepresentationModel<CoachDTO> {
    private final long id;
    private final String name;
    private final String surname;
    private final String email;
    private final String phone;
}
