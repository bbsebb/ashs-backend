package fr.hoenheimsports.trainingservice.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "halls")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class HallDTO extends RepresentationModel<HallDTO> {
    private final long id;
    private final String name;
    private final AddressDTO address;
}
