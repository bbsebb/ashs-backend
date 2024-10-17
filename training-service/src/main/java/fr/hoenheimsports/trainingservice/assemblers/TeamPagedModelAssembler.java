package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.dto.TeamDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface TeamPagedModelAssembler extends ModelAssembler<Page<EntityModel<TeamDto>>, PagedModel<?>>{
}
