package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.dto.HallDTO;
import fr.hoenheimsports.trainingservice.dto.request.HallDTORequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface HallController {
    @GetMapping
    PagedModel<HallDTO> getAllHalls(Pageable pageable);

    @GetMapping("/{id}")
    HallDTO getHallById(@PathVariable Long id);

    @PostMapping
    HallDTO createHall(@RequestBody HallDTORequest newHall);

    @PutMapping("/{id}")
    HallDTO updateHall(@PathVariable Long id, @RequestBody HallDTORequest newHall);

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteHall(@PathVariable Long id);
}
