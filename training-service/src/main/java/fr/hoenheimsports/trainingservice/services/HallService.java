package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.ressources.HallModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface HallService {
    HallModel createHall(HallDto hallDto);

    PagedModel<HallModel> getAllHalls(int page, int size, List<String> sort);

    HallModel getHallById(Long id) throws HallNotFoundException;

    HallModel updateHall(Long id, HallDto hallDto) throws HallNotFoundException;

    void deleteHall(Long id);
}
