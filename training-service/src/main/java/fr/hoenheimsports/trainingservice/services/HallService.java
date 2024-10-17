package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public interface HallService {
    EntityModel<HallDto> createHall(HallDto hallDto);

    PagedModel<?> getAllHalls(int page, int size, List<String> sort);

    EntityModel<HallDto> getHallById(Long id) throws HallNotFoundException;

    EntityModel<HallDto> updateHall(Long id, HallDto hallDto) throws HallNotFoundException;

    void deleteHall(Long id);
}
