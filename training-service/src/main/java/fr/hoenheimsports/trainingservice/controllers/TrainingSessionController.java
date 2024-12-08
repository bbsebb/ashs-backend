package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.dto.TrainingSessionDTO;
import fr.hoenheimsports.trainingservice.dto.request.TrainingSessionDTORequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface TrainingSessionController {
    @GetMapping
    PagedModel<?> getAllTrainingSessions(Pageable pageable);

    @GetMapping("/{id}")
    TrainingSessionDTO getTrainingSessionById(@PathVariable Long id);

    @PostMapping
    TrainingSessionDTO createTrainingSession(@RequestBody TrainingSessionDTORequest newTrainingSession);

    @PutMapping("/{id}")
    TrainingSessionDTO updateTrainingSession(@PathVariable Long id, @RequestBody TrainingSessionDTORequest newTrainingSession);

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTrainingSession(@PathVariable Long id);
}