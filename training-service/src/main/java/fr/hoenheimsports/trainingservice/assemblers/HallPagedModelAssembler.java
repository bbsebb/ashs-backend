package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.ressources.HallModel;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;

public interface HallPagedModelAssembler extends ModelAssembler<Page<HallModel>, PagedModel<HallModel>>{
}
