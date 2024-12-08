package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.dto.HallDTO;
import fr.hoenheimsports.trainingservice.dto.request.HallDTORequest;
import fr.hoenheimsports.trainingservice.models.Hall;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

public interface HallService {
    HallDTO createAndConvertToModel(HallDTORequest hallDtoRequest);

    Hall findOrCreateOrUpdate(HallDTORequest hallDtoRequest);

    PagedModel<HallDTO> getAllModels(Pageable pageable);

    HallDTO getModelById(Long id);

    HallDTO updateAndConvertToModel(Long id, HallDTORequest hallDtoRequest);

    void deleteById(Long id);
}
