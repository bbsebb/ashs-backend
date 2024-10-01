package fr.hoenheimsports.trainingservice.assemblers;

import org.springframework.hateoas.RepresentationModel;

public interface ModelAssembler <T, U extends RepresentationModel<?>>{

    U toModel( T entity);

}
