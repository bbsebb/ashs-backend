package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.CoachControllerImpl;
import fr.hoenheimsports.trainingservice.dto.CoachDTO;
import fr.hoenheimsports.trainingservice.dto.request.CoachDTORequest;
import fr.hoenheimsports.trainingservice.mappers.CoachMapper;
import fr.hoenheimsports.trainingservice.models.Coach;
import fr.hoenheimsports.trainingservice.services.UserSecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CoachAssemblerImpl implements CoachAssembler {
    private final CoachMapper coachMapper;
    private final PagedResourcesAssembler<Coach> pagedResourcesAssembler;
    private final UserSecurityService userSecurityService;

    public CoachAssemblerImpl(CoachMapper coachMapper, PagedResourcesAssembler<Coach> pagedResourcesAssembler, UserSecurityService userSecurityService) {
        this.coachMapper = coachMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userSecurityService = userSecurityService;
    }

    @NonNull
    @Override
    public CoachDTO toModel(@NonNull Coach entity) {
        CoachDTO CoachDto = this.coachMapper.toDto(entity);
        this.addLinks(CoachDto);
        return CoachDto;
    }

    @NonNull
    @Override
    public CollectionModel<CoachDTO> toCollectionModel(@NonNull Iterable<? extends Coach> entities) {
        var resources = CoachAssembler.super.toCollectionModel(entities);
        this.addLinks(resources);
        return resources;
    }

    @Override
    public PagedModel<CoachDTO> toPagedModel(Page<Coach> page) {
        PagedModel<CoachDTO> resources = this.pagedResourcesAssembler.toModel(page,this);
        this.addLinks(resources);
        return resources;
    }

    public void addLinks(CollectionModel<CoachDTO> resources) {
        resources.add(linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(Pageable.unpaged())).withRel("coaches").expand());
        if(!this.userSecurityService.hasRole("ADMIN")) {
            resources.add(linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(Pageable.unpaged())).withRel("coaches").expand()
                    .andAffordance(afford(methodOn(CoachControllerImpl.class).createCoach(new CoachDTORequest(1L, "email", "name", "phone", "surname")))) // default name
                    .andAffordance(afford(methodOn(CoachControllerImpl.class).createCoach(new CoachDTORequest(1L, "email", "name", "phone", "surname")))));

        }
        }

    public void addLinks(CoachDTO ressource) {
        // Ajoute les liens par défaut accessibles pour tous les utilisateurs
        ressource.add(linkTo(methodOn(CoachControllerImpl.class).getCoachById(ressource.getId())).withSelfRel());
        ressource.add(linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(Pageable.unpaged())).withRel("coaches").expand());
        if(this.userSecurityService.hasRole("ADMIN")) {
            ressource.add(linkTo(methodOn(CoachControllerImpl.class).getCoachById(ressource.getId())).withSelfRel()
                    .andAffordance(afford(methodOn(CoachControllerImpl.class).updateCoach(ressource.getId(), null))) // default
                    .andAffordance(afford(methodOn(CoachControllerImpl.class).updateCoach(ressource.getId(), null)))
                    .andAffordance(afford(methodOn(CoachControllerImpl.class).deleteCoach(ressource.getId()))));
        }
    }

}
