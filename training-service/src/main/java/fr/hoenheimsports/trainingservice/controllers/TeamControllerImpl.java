package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TeamDTO;
import fr.hoenheimsports.trainingservice.dto.request.TeamDTORequest;
import fr.hoenheimsports.trainingservice.services.TeamServiceImpl;
import fr.hoenheimsports.trainingservice.validators.OnCreate;
import fr.hoenheimsports.trainingservice.validators.OnUpdate;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public PagedModel<TeamDTO> getAllTeams(@ParameterObject Pageable pageable) {

        return teamService.getAllModels(pageable);
    }

    @Override
    @GetMapping("/{id}")
    public TeamDTO getTeamById(@PathVariable Long id) throws TeamNotFoundException {
        return teamService.getModelById(id);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TeamDTO createTeam(@Validated(OnCreate.class) @RequestBody TeamDTORequest newTeam) throws TrainingSessionNotFoundException, CoachNotFoundException, HallNotFoundException {
        return teamService.createAndConvertToModel(newTeam);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TeamDTO updateTeam(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody TeamDTORequest newTeam) throws TeamNotFoundException, CoachNotFoundException, TrainingSessionNotFoundException, HallNotFoundException {
        return teamService.updateAndConvertToModel(id, newTeam);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}