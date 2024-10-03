package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import fr.hoenheimsports.trainingservice.ressources.CoachModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface CoachService {
    CoachModel createCoach(CoachDto coachDto);

    PagedModel<CoachModel> getAllCoaches(int page, int size, List<String> sort);

    CoachModel getCoachById(Long id) throws CoachNotFoundException;

    CoachModel updateCoach(Long id, CoachDto coachDto) throws CoachNotFoundException;

    void deleteCoach(Long id);
}
