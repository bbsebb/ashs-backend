package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.ressources.TeamModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface TeamPagedModelAssembler extends ModelAssembler<Page<TeamModel>, PagedModel<TeamModel>>{
}
