package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.controllers.CoachControllerImpl;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import fr.hoenheimsports.trainingservice.ressources.CoachModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CoachModelAssemblerImpl implements CoachModelAssembler{

    public CoachModel toModel( CoachDto coachDto)  {
        CoachModel coachModel = new CoachModel(coachDto);
        try {
            coachModel.add(
                    linkTo(methodOn(CoachControllerImpl.class).getCoachById(coachDto.id())).withSelfRel()
                            .andAffordance(afford(methodOn(CoachControllerImpl.class).updateCoach(coachDto.id(), null))) // default skip
                            .andAffordance(afford(methodOn(CoachControllerImpl.class).updateCoach(coachDto.id(), null)))
                            .andAffordance(afford(methodOn(CoachControllerImpl.class).deleteCoach(coachDto.id())))

            );
        } catch (CoachNotFoundException e) {
            throw new RuntimeException(e);
        }

        coachModel.add(
                linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(0, 20, null)).withRel("coaches").expand()
        );

        return coachModel;
    }
}
