package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.controllers.HallControllerImpl;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class HallModelAssemblerImpl implements HallModelAssembler {
    @Override
    public EntityModel<HallDto> toModel(HallDto entity) {
        EntityModel<HallDto> hallModel = EntityModel.of(entity);
        try {
            hallModel.add(
                    linkTo(methodOn(HallControllerImpl.class).getHallById(entity.id())).withSelfRel()
                            .andAffordance(afford(methodOn(HallControllerImpl.class).updateHall(entity.id(), null))) //skip default
                            .andAffordance(afford(methodOn(HallControllerImpl.class).updateHall(entity.id(), null)))
                            .andAffordance(afford(methodOn(HallControllerImpl.class).deleteHall(entity.id())))

            );
        } catch (HallNotFoundException e) {
            throw new RuntimeException(e);
        }

        hallModel.add(
                linkTo(methodOn(HallControllerImpl.class).getAllHalls(0, 20, null)).withRel("halls").expand()
        );
        return hallModel;
    }
}
