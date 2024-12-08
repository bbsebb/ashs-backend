package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.dto.CoachDTO;
import fr.hoenheimsports.trainingservice.dto.request.CoachDTORequest;

import fr.hoenheimsports.trainingservice.services.CoachService;
import fr.hoenheimsports.trainingservice.validators.OnCreate;
import fr.hoenheimsports.trainingservice.validators.OnUpdate;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/coaches",
        produces = "application/prs.hal-forms+json")
public class CoachControllerImpl implements CoachController {

    private final CoachService coachService;

    public CoachControllerImpl(CoachService coachService) {
        this.coachService = coachService;
    }

    @Override
    @GetMapping
    public PagedModel<CoachDTO> getAllCoaches(@ParameterObject Pageable pageable) {
        return coachService.getAllModels(pageable);
    }

    @Override
    @GetMapping("/{id}")
    public CoachDTO getCoachById(@PathVariable Long id) {
        return coachService.getModelById(id);
    }

    @Override
    @PostMapping
    public CoachDTO createCoach(@Validated(OnCreate.class) @RequestBody CoachDTORequest newCoach) {
        return coachService.createAndConvertToModel(newCoach);
    }

    @Override
    @PutMapping("/{id}")
    public CoachDTO updateCoach(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody CoachDTORequest newCoach) {
        return coachService.updateAndConvertToModel(id, newCoach);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoach(@PathVariable Long id) {
        coachService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}