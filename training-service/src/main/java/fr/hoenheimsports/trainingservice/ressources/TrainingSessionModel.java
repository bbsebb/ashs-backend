package fr.hoenheimsports.trainingservice.ressources;

import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import org.springframework.hateoas.EntityModel;


public class TrainingSessionModel extends EntityModel<TrainingSessionDto> {
    public TrainingSessionModel(TrainingSessionDto content) {
        super(content);
    }
}
