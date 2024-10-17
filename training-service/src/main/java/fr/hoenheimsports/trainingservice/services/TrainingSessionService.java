package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface TrainingSessionService {
    EntityModel<TrainingSessionDto> createTrainingSession(TrainingSessionDto trainingSessionDto) ;

    PagedModel<?> getAllTrainingSessions(int page, int size, List<String> sort);

    EntityModel<TrainingSessionDto> getTrainingSessionById(Long id) throws TrainingSessionNotFoundException;

    EntityModel<TrainingSessionDto> updateTrainingSession(Long id, TrainingSessionDto trainingSessionDto) throws TrainingSessionNotFoundException;

    void deleteTrainingSession(Long id);
}