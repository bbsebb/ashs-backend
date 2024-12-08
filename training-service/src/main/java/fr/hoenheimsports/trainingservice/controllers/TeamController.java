package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.dto.TeamDTO;
import fr.hoenheimsports.trainingservice.dto.request.TeamDTORequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface TeamController {
    @GetMapping
    PagedModel<TeamDTO> getAllTeams(Pageable pageable);

    @GetMapping("/{id}")
    TeamDTO getTeamById(@PathVariable Long id);

    @PostMapping
    TeamDTO createTeam(@RequestBody TeamDTORequest newTeam);

    @PutMapping("/{id}")
    TeamDTO updateTeam(@PathVariable Long id, @RequestBody TeamDTORequest newTeam);

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTeam(@PathVariable Long id);
}