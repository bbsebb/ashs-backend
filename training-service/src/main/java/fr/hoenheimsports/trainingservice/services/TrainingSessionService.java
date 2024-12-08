package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.dto.TrainingSessionDTO;
import fr.hoenheimsports.trainingservice.dto.request.TrainingSessionDTORequest;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

public interface TrainingSessionService {
    TrainingSessionDTO createAndConvertToModel(TrainingSessionDTORequest trainingSessionDtoRequest);

    TrainingSession findOrCreateOrUpdate(TrainingSessionDTORequest trainingSessionDtoRequest);


    PagedModel<TrainingSessionDTO> getAllModels(Pageable pageable);

    Page<TrainingSession> getAll(Pageable pageable);

    TrainingSessionDTO getModelById(Long id);

    TrainingSessionDTO updateAndConvertToModel(Long id, TrainingSessionDTORequest trainingSessionDtoRequest);

    void deleteById(Long id);
}