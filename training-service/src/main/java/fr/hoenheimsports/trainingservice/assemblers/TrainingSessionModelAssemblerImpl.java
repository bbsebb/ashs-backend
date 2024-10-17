package fr.hoenheimsports.trainingservice.assemblers;


import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.controllers.HallControllerImpl;
import fr.hoenheimsports.trainingservice.controllers.TrainingSessionControllerImpl;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TrainingSessionModelAssemblerImpl implements TrainingSessionModelAssembler{
    @Override
    public EntityModel<TrainingSessionDto> toModel(TrainingSessionDto entity) {
        EntityModel<TrainingSessionDto> trainingSessionModel = EntityModel.of(entity);
        try {
            trainingSessionModel.add(
                    linkTo(methodOn(TrainingSessionControllerImpl.class).getTrainingSessionById(entity.id())).withSelfRel()
                            .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).updateTrainingSession(entity.id(), null))) //skip default
                            .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).updateTrainingSession(entity.id(), null)))
                            .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).deleteTrainingSession(entity.id())))
            );
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }

        trainingSessionModel.add(
                linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(0, 20, null)).withRel("trainingSessions").expand()
        );

        try {
            if(entity.hall() != null) {
                trainingSessionModel.add(
                        linkTo(methodOn(HallControllerImpl.class).getHallById(entity.hall().id())).withRel("hall").expand()
                );
            }
        } catch (HallNotFoundException e) {
            throw new RuntimeException(e);
        }
        return trainingSessionModel;
    }
}
