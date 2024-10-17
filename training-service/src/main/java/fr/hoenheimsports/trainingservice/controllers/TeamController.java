package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TeamController {
    @GetMapping
    PagedModel<?> getAllTeams(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size,
                                      @RequestParam(name = "sort", required = false) List<String> sort);

    @GetMapping("/{id}")
    EntityModel<TeamDto> getTeamById(@PathVariable Long id) throws TeamNotFoundException;

    @PostMapping
    EntityModel<TeamDto> createTeam(@RequestBody TeamDto newTeam)  ;

    @PutMapping("/{id}")
    EntityModel<TeamDto> updateTeam(@PathVariable Long id, @RequestBody TeamDto newTeam) throws TeamNotFoundException;

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTeam(@PathVariable Long id);
}