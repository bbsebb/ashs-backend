package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface TeamService {
    EntityModel<TeamDto> createTeam(TeamDto teamDto)  ;

    PagedModel<?> getAllTeams(int page, int size, List<String> sort);

    EntityModel<TeamDto> getTeamById(Long id) throws TeamNotFoundException;

    EntityModel<TeamDto> updateTeam(Long id, TeamDto teamDto) throws TeamNotFoundException;

    void deleteTeam(Long id);
}