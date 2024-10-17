package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TrainingSessionController {
    @GetMapping
    PagedModel<?> getAllTrainingSessions(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size,
                                                            @RequestParam(name = "sort", required = false) List<String> sort);

    @GetMapping("/{id}")
    EntityModel<TrainingSessionDto> getTrainingSessionById(@PathVariable Long id) throws TrainingSessionNotFoundException;

    @PostMapping
    EntityModel<TrainingSessionDto> createTrainingSession(@RequestBody TrainingSessionDto newTrainingSession)  ;

    @PutMapping("/{id}")
    EntityModel<TrainingSessionDto> updateTrainingSession(@PathVariable Long id, @RequestBody TrainingSessionDto newTrainingSession) throws TrainingSessionNotFoundException;

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTrainingSession(@PathVariable Long id);
}