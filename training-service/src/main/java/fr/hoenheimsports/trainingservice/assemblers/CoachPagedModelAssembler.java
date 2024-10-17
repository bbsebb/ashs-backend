package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.dto.CoachDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface CoachPagedModelAssembler extends  ModelAssembler<Page<EntityModel<CoachDto>>, PagedModel<?>>{
    PagedModel<?> toModel(Page<EntityModel<CoachDto>> entity);
}
