package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.dto.HallDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface HallPagedModelAssembler extends ModelAssembler<Page<EntityModel<HallDto>>, PagedModel<?>>{
}
