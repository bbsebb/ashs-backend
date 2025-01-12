package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.TeamControllerImpl;
import fr.hoenheimsports.trainingservice.dto.TeamDTO;
import fr.hoenheimsports.trainingservice.dto.request.TeamDTORequest;
import fr.hoenheimsports.trainingservice.mappers.TeamMapper;
import fr.hoenheimsports.trainingservice.models.Category;
import fr.hoenheimsports.trainingservice.models.Gender;
import fr.hoenheimsports.trainingservice.models.Team;
import fr.hoenheimsports.trainingservice.services.UserSecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TeamAssemblerImpl implements TeamAssembler {

    private final TeamMapper teamMapper;
    private final PagedResourcesAssembler<Team> pagedResourcesAssembler;
    private final UserSecurityService userSecurityService;

    public TeamAssemblerImpl(TeamMapper teamMapper, PagedResourcesAssembler<Team> pagedResourcesAssembler, UserSecurityService userSecurityService) {
        this.teamMapper = teamMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userSecurityService = userSecurityService;
    }


    @NonNull
    @Override
    public TeamDTO toModel(@NonNull Team entity) {
        TeamDTO teamDTORequest = this.teamMapper.toDto(entity);
        this.addLinks(teamDTORequest);
        return teamDTORequest;
    }

    @NonNull
    @Override
    public CollectionModel<TeamDTO> toCollectionModel(@NonNull Iterable<? extends Team> entities) {
        var resources = TeamAssembler.super.toCollectionModel(entities);
        this.addLinks(resources);
        return resources;
    }


    @Override
    public PagedModel<TeamDTO> toPagedModel(Page<Team> page) {
        PagedModel<TeamDTO> resources = this.pagedResourcesAssembler.toModel(page,this);
        this.addLinks(resources);
        return resources;
    }

    private void addLinks(CollectionModel<TeamDTO> resources) {
        resources.add(linkTo(methodOn(TeamControllerImpl.class).getAllTeams(Pageable.unpaged())).withSelfRel());
        if(this.userSecurityService.hasRole("ADMIN")) {
            resources.add(linkTo(methodOn(TeamControllerImpl.class).getAllTeams(Pageable.unpaged())).withSelfRel()
                    .andAffordance(afford(methodOn(TeamControllerImpl.class).createTeam(new TeamDTORequest(1L, Gender.N, Category.SENIOR,1, Set.of(), Set.of())))) // skip default name
                    .andAffordance(afford(methodOn(TeamControllerImpl.class).createTeam(new TeamDTORequest(1L, Gender.N, Category.SENIOR,1,Set.of(), Set.of()))))
            );
        }

    }

    private void addLinks(TeamDTO resource) {
        resource.add(linkTo(methodOn(TeamControllerImpl.class).getTeamById(resource.getId())).withSelfRel());
        resource.add(linkTo(methodOn(TeamControllerImpl.class).getAllTeams(Pageable.unpaged())).withRel("teams").expand());
        if(this.userSecurityService.hasRole("ADMIN")) {
            resource.add(linkTo(methodOn(TeamControllerImpl.class).getTeamById(resource.getId())).withSelfRel()
                    .andAffordance(afford(methodOn(TeamControllerImpl.class).updateTeam(resource.getId(), null))) //skip default
                    .andAffordance(afford(methodOn(TeamControllerImpl.class).updateTeam(resource.getId(), null)))
                    .andAffordance(afford(methodOn(TeamControllerImpl.class).deleteTeam(resource.getId()))));
        }

    }
}
