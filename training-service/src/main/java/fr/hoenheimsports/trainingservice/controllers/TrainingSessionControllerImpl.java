package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import fr.hoenheimsports.trainingservice.services.TrainingSessionServiceImpl;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public PagedModel<?> getAllTrainingSessions(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestParam(name = "sort", required = false) List<String> sort) {

        return trainingSessionService.getAllTrainingSessions(page, size, sort);
    }

    @Override
    @GetMapping("/{id}")
    public EntityModel<TrainingSessionDto> getTrainingSessionById(@PathVariable Long id) throws TrainingSessionNotFoundException {
        return trainingSessionService.getTrainingSessionById(id);
    }

    @Override
    @PostMapping
    public EntityModel<TrainingSessionDto> createTrainingSession(@RequestBody TrainingSessionDto newTrainingSession)   {
        return trainingSessionService.createTrainingSession(newTrainingSession);
    }

    @Override
    @PutMapping("/{id}")
    public EntityModel<TrainingSessionDto> updateTrainingSession(@PathVariable Long id, @RequestBody TrainingSessionDto newTrainingSession) throws TrainingSessionNotFoundException {
        return trainingSessionService.updateTrainingSession(id, newTrainingSession);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrainingSession(@PathVariable Long id) {
        trainingSessionService.deleteTrainingSession(id);
        return ResponseEntity.noContent().build();
    }
}