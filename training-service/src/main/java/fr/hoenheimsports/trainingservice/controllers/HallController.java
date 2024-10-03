package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.ressources.HallModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface HallController {
    @GetMapping
    PagedModel<HallModel> getAllHalls(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size,
                                      @RequestParam(name = "sort", required = false) List<String> sort);

    @GetMapping("/{id}")
    HallModel getHallById(@PathVariable Long id) throws HallNotFoundException;

    @PostMapping
    HallModel createHall(@RequestBody HallDto newHall);

    @PutMapping("/{id}")
    HallModel updateHall(@PathVariable Long id, @RequestBody HallDto newHall) throws HallNotFoundException;

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteHall(@PathVariable Long id);
}
