package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.ressources.CoachModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface CoachPagedModelAssembler extends  ModelAssembler<Page<CoachModel>, PagedModel<CoachModel>>{
}
