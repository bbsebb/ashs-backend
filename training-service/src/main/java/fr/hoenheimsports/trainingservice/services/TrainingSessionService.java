package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import fr.hoenheimsports.trainingservice.ressources.TrainingSessionModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface TrainingSessionService {
    TrainingSessionModel createTrainingSession(TrainingSessionDto trainingSessionDto);

    PagedModel<TrainingSessionModel> getAllTrainingSessions(int page, int size, List<String> sort);

    TrainingSessionModel getTrainingSessionById(Long id) throws TrainingSessionNotFoundException;

    TrainingSessionModel updateTrainingSession(Long id, TrainingSessionDto trainingSessionDto) throws TrainingSessionNotFoundException;

    void deleteTrainingSession(Long id);
}