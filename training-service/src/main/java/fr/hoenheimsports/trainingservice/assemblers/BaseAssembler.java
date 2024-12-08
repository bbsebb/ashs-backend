package fr.hoenheimsports.trainingservice.assemblers;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface BaseAssembler<T, D extends RepresentationModel<?>> extends RepresentationModelAssembler<T, D> {
    PagedModel<D> toPagedModel(Page<T> page);
}
