package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.CoachControllerImpl;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import fr.hoenheimsports.trainingservice.ressources.CoachModel;
import fr.hoenheimsports.trainingservice.services.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CoachPagedModelAssemblerImpl implements CoachPagedModelAssembler{

    private final SortUtil sortUtil;

    public CoachPagedModelAssemblerImpl(SortUtil sortUtil) {
        this.sortUtil = sortUtil;
    }

    @Override
    public PagedModel<CoachModel> toModel(Page<CoachModel> entity) {
        List<CoachModel> coachModels = entity.getContent();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                entity.getSize(), entity.getNumber(), entity.getTotalElements(), entity.getTotalPages());
        List<String> sort = this.sortUtil.createSortParams(entity.getSort());

        PagedModel<CoachModel> pagedModel = PagedModel.of(coachModels, pageMetadata);

        // Add self link
        pagedModel.add(linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(entity.getNumber(), entity.getSize(), sort)).withSelfRel()
                .andAffordance(afford(methodOn(CoachControllerImpl.class).createCoach(new CoachDto(1L, "email", "name", "phone", "surname")))) // default name
                .andAffordance(afford(methodOn(CoachControllerImpl.class).createCoach(new CoachDto(1L, "email", "name", "phone", "surname"))))
        );

        // Add next link if there is a next page
        if (entity.hasNext()) {
            pagedModel.add(linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(entity.getNumber() + 1, entity.getSize(), sort)).withRel("next").expand());
        }

        // Add prev link if there is a previous page
        if (entity.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(entity.getNumber() - 1, entity.getSize(), sort)).withRel("prev").expand());
        }

        // Add first link
        pagedModel.add(linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(0, entity.getSize(), sort)).withRel("first").expand());

        // Add last link
        int lastPage = entity.getTotalPages() - 1;
        pagedModel.add(linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(lastPage, entity.getSize(), sort)).withRel("last").expand());

        return pagedModel;
    }
}