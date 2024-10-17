package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface CoachService {
    EntityModel<CoachDto> createCoach(CoachDto coachDto);

    PagedModel<?> getAllCoaches(int page, int size, List<String> sort);

    EntityModel<CoachDto> getCoachById(Long id) throws CoachNotFoundException;

    EntityModel<CoachDto> updateCoach(Long id, CoachDto coachDto) throws CoachNotFoundException;

    void deleteCoach(Long id);
}
