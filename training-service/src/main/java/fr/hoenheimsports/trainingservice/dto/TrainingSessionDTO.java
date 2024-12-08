package fr.hoenheimsports.trainingservice.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "trainingSessions")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class TrainingSessionDTO extends RepresentationModel<TrainingSessionDTO> {
    private final long id;
    private final TimeSlotDTO timeSlot;
    private final HallDTO hall;
}
