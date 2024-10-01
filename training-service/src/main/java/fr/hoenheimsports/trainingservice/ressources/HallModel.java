package fr.hoenheimsports.trainingservice.ressources;

import fr.hoenheimsports.trainingservice.dto.HallDto;
import org.springframework.hateoas.EntityModel;

public class HallModel extends EntityModel<HallDto> {
    public HallModel(HallDto content) {
        super(content);
    }
}
