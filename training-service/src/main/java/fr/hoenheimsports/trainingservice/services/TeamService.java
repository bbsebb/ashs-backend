package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.dto.TeamDTO;
import fr.hoenheimsports.trainingservice.dto.request.TeamDTORequest;
import fr.hoenheimsports.trainingservice.models.Team;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

public interface TeamService {
    Team findOrCreateOrUpdate(TeamDTORequest teamDtoRequest) throws TeamNotFoundException, CoachNotFoundException, TrainingSessionNotFoundException, HallNotFoundException;

    TeamDTO createAndConvertToModel(TeamDTORequest teamDtoRequest) throws TrainingSessionNotFoundException, CoachNotFoundException, HallNotFoundException;

    PagedModel<TeamDTO> getAllModels(Pageable pageable);

    TeamDTO getModelById(Long id) throws TeamNotFoundException;

    TeamDTO updateAndConvertToModel(Long id, TeamDTORequest teamDtoRequest) throws TeamNotFoundException, CoachNotFoundException, TrainingSessionNotFoundException, HallNotFoundException;

    void deleteById(Long id);
}