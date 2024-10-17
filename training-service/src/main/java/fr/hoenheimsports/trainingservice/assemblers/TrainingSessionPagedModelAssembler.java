package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface TrainingSessionPagedModelAssembler extends ModelAssembler<Page<EntityModel<TrainingSessionDto>>, PagedModel<?>>{
}
