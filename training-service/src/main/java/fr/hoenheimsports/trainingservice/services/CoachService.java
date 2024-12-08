package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.dto.CoachDTO;
import fr.hoenheimsports.trainingservice.dto.request.CoachDTORequest;
import fr.hoenheimsports.trainingservice.models.Coach;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

public interface CoachService {
    CoachDTO createAndConvertToModel(CoachDTORequest coachDtoRequest);

    Coach findOrCreateOrUpdate(CoachDTORequest coachDtoRequest);

    PagedModel<CoachDTO> getAllModels(Pageable pageable);

    CoachDTO getModelById(Long id);

    CoachDTO updateAndConvertToModel(Long id, CoachDTORequest coachDtoRequest);

    void deleteById(Long id);
}
