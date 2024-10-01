package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.ressources.TrainingSessionModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface TrainingSessionPagedModelAssembler extends ModelAssembler<Page<TrainingSessionModel>, PagedModel<TrainingSessionModel>>{
}
