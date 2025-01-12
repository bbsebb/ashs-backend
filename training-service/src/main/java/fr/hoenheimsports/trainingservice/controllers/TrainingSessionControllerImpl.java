package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.dto.TrainingSessionDTO;
import fr.hoenheimsports.trainingservice.dto.request.TrainingSessionDTORequest;
import fr.hoenheimsports.trainingservice.services.TrainingSessionServiceImpl;
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
@RequestMapping(path = "/trainingSessions",
        produces = "application/prs.hal-forms+json")
public class TrainingSessionControllerImpl implements TrainingSessionController {

    private final TrainingSessionServiceImpl trainingSessionService;

    public TrainingSessionControllerImpl(TrainingSessionServiceImpl trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }

    @Override
    @GetMapping
    public PagedModel<TrainingSessionDTO> getAllTrainingSessions(@ParameterObject Pageable pageable) {
        return trainingSessionService.getAllModels(pageable);
    }

    @Override
    @GetMapping("/{id}")
    public TrainingSessionDTO getTrainingSessionById(@PathVariable Long id)  {
        return trainingSessionService.getModelById(id);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TrainingSessionDTO createTrainingSession(@Validated(OnCreate.class)  @RequestBody TrainingSessionDTORequest newTrainingSession) {
        return trainingSessionService.createAndConvertToModel(newTrainingSession);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TrainingSessionDTO updateTrainingSession(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody TrainingSessionDTORequest newTrainingSession) {
        return trainingSessionService.updateAndConvertToModel(id, newTrainingSession);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTrainingSession(@PathVariable Long id) {
        trainingSessionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}