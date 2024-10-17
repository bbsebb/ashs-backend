package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.controllers.CoachControllerImpl;
import fr.hoenheimsports.trainingservice.controllers.TeamControllerImpl;
import fr.hoenheimsports.trainingservice.controllers.TrainingSessionControllerImpl;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TeamModelAssemblerImpl implements TeamModelAssembler{
    @Override
    public EntityModel<TeamDto> toModel(TeamDto entity) {
        EntityModel<TeamDto> teamModel = EntityModel.of(entity);
        try {
            teamModel.add(
                    linkTo(methodOn(TeamControllerImpl.class).getTeamById(entity.id())).withSelfRel()
                            .andAffordance(afford(methodOn(TeamControllerImpl.class).updateTeam(entity.id(), null))) //skip default
                            .andAffordance(afford(methodOn(TeamControllerImpl.class).updateTeam(entity.id(), null)))
                            .andAffordance(afford(methodOn(TeamControllerImpl.class).deleteTeam(entity.id())))
            );
        } catch (TeamNotFoundException e) {
            throw new RuntimeException(e);
        }

        teamModel.add(
                linkTo(methodOn(TeamControllerImpl.class).getAllTeams(0, 20, null)).withRel("teams").expand()
        );

        try {
            teamModel.add(
                    linkTo(methodOn(CoachControllerImpl.class).getCoachById(entity.coach().id())).withRel("coach")
            );
        } catch (CoachNotFoundException e) {
            throw new RuntimeException(e);
        }

        entity.trainingSessions().forEach(trainingSessionDto -> {
            try {
                teamModel.add(
                        linkTo(methodOn(TrainingSessionControllerImpl.class).getTrainingSessionById(trainingSessionDto.id())).withRel("trainingSessions")
                );
            } catch (TrainingSessionNotFoundException e) {
                throw new RuntimeException(e);
            }
        });


        return teamModel;
    }
}