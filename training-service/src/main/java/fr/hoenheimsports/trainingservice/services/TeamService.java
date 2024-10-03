package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import fr.hoenheimsports.trainingservice.ressources.TeamModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface TeamService {
    TeamModel createTeam(TeamDto teamDto);

    PagedModel<TeamModel> getAllTeams(int page, int size, List<String> sort);

    TeamModel getTeamById(Long id) throws TeamNotFoundException;

    TeamModel updateTeam(Long id, TeamDto teamDto) throws TeamNotFoundException;

    void deleteTeam(Long id);
}