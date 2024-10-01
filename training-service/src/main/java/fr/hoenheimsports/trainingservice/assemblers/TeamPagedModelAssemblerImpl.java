package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.TeamControllerImpl;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import fr.hoenheimsports.trainingservice.models.Category;
import fr.hoenheimsports.trainingservice.models.Gender;
import fr.hoenheimsports.trainingservice.ressources.TeamModel;
import fr.hoenheimsports.trainingservice.services.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TeamPagedModelAssemblerImpl implements TeamPagedModelAssembler{
    private final SortUtil sortUtil;

    public TeamPagedModelAssemblerImpl(SortUtil sortUtil) {
        this.sortUtil = sortUtil;
    }

    @Override
    public PagedModel<TeamModel> toModel(Page<TeamModel> entity) {

        List<TeamModel> teamModels = entity.getContent();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                entity.getSize(), entity.getNumber(), entity.getTotalElements(), entity.getTotalPages());
        PagedModel<TeamModel> pagedModel = PagedModel.of(teamModels, pageMetadata);
        List<String> sort = this.sortUtil.createSortParams(entity.getSort());

        // Add self link
        pagedModel.add(linkTo(methodOn(TeamControllerImpl.class).getAllTeams(entity.getNumber(), entity.getSize(), sort)).withSelfRel()
                .andAffordance(afford(methodOn(TeamControllerImpl.class).createTeam(new TeamDto(1L, Gender.N, Category.SENIOR,1,new CoachDto(1L, "email", "name", "phone", "surname"), Set.of())))) // skip default name
                .andAffordance(afford(methodOn(TeamControllerImpl.class).createTeam(new TeamDto(1L, Gender.N, Category.SENIOR,1,new CoachDto(1L, "email", "name", "phone", "surname"), Set.of()))))
        );

        // Add next link if there is a next page
        if (entity.hasNext()) {
            pagedModel.add(linkTo(methodOn(TeamControllerImpl.class).getAllTeams(entity.getNumber() + 1, entity.getSize(), sort)).withRel("next").expand());
        }

        // Add prev link if there is a previous page
        if (entity.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(TeamControllerImpl.class).getAllTeams(entity.getNumber() - 1, entity.getSize(), sort)).withRel("prev").expand());
        }

        // Add first link
        pagedModel.add(linkTo(methodOn(TeamControllerImpl.class).getAllTeams(0, entity.getSize(), sort)).withRel("first").expand());

        // Add last link
        int lastPage = entity.getTotalPages() - 1;
        pagedModel.add(linkTo(methodOn(TeamControllerImpl.class).getAllTeams(lastPage, entity.getSize(), sort)).withRel("last").expand());

        return pagedModel;
    }
}