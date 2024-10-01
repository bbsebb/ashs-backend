package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import fr.hoenheimsports.trainingservice.ressources.CoachModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CoachController {
    @GetMapping
    PagedModel<CoachModel> getAllCoaches(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size,
                                         @RequestParam(name = "sort", required = false) List<String> sort);

    @GetMapping("/{id}")
    CoachModel getCoachById(@PathVariable Long id) throws CoachNotFoundException;

    @PostMapping
    CoachModel createCoach(@RequestBody CoachDto newCoach);

    @PutMapping("/{id}")
    CoachModel updateCoach(@PathVariable Long id, @RequestBody CoachDto newCoach) throws CoachNotFoundException;

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCoach(@PathVariable Long id) ;
}
