package fr.hoenheimsports.trainingservice.ressources;

import fr.hoenheimsports.trainingservice.dto.CoachDto;
import org.springframework.hateoas.EntityModel;

public class CoachModel extends EntityModel<CoachDto> {
    public CoachModel(CoachDto content) {
        super(content);
    }
}
