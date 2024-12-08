package fr.hoenheimsports.trainingservice.dto;

import fr.hoenheimsports.trainingservice.models.Category;
import fr.hoenheimsports.trainingservice.models.Gender;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import java.util.Set;

@Relation(collectionRelation = "teams")
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class TeamDTO extends RepresentationModel<TeamDTO> {
    private final long id;
    private final Gender gender;
    private final Category category;
    private final int teamNumber;
    private final Set<CoachDTO> coaches;
    private final Set<TrainingSessionDTO> trainingSessions;
 }
