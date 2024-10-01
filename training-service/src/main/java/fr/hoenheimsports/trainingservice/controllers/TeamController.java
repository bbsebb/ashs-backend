package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import fr.hoenheimsports.trainingservice.ressources.TeamModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TeamController {
    @GetMapping
    PagedModel<TeamModel> getAllTeams(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size,
                                      @RequestParam(name = "sort", required = false) List<String> sort);

    @GetMapping("/{id}")
    TeamModel getTeamById(@PathVariable Long id) throws TeamNotFoundException;

    @PostMapping
    TeamModel createTeam(@RequestBody TeamDto newTeam);

    @PutMapping("/{id}")
    TeamModel updateTeam(@PathVariable Long id, @RequestBody TeamDto newTeam) throws TeamNotFoundException;

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTeam(@PathVariable Long id);
}