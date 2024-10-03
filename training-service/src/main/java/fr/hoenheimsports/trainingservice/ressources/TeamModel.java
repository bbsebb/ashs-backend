package fr.hoenheimsports.trainingservice.ressources;

import fr.hoenheimsports.trainingservice.dto.TeamDto;
import org.springframework.hateoas.EntityModel;

public class TeamModel extends EntityModel<TeamDto> {
    public TeamModel(TeamDto content) {
        super(content);
    }
}
