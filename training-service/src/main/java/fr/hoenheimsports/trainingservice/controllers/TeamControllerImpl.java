package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import fr.hoenheimsports.trainingservice.ressources.TeamModel;
import fr.hoenheimsports.trainingservice.services.TeamServiceImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/teams",
        produces = "application/prs.hal-forms+json")
public class TeamControllerImpl implements TeamController {
    private final TeamServiceImpl teamService;

    public TeamControllerImpl(TeamServiceImpl teamService) {
        this.teamService = teamService;
    }

    @Override
    @GetMapping
    public PagedModel<TeamModel> getAllTeams(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size,
                                             @RequestParam(name = "sort", required = false) List<String> sort) {

        return teamService.getAllTeams(page, size, sort);
    }

    @Override
    @GetMapping("/{id}")
    public TeamModel getTeamById(@PathVariable Long id) throws TeamNotFoundException {
        return teamService.getTeamById(id);
    }

    @Override
    @PostMapping
    public TeamModel createTeam(@RequestBody TeamDto newTeam) {
        return teamService.createTeam(newTeam);
    }

    @Override
    @PutMapping("/{id}")
    public TeamModel updateTeam(@PathVariable Long id, @RequestBody TeamDto newTeam) throws TeamNotFoundException {
        return teamService.updateTeam(id, newTeam);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}