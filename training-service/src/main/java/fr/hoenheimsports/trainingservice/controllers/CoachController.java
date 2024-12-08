package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.dto.CoachDTO;
import fr.hoenheimsports.trainingservice.dto.request.CoachDTORequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface CoachController {
    @GetMapping
    PagedModel<CoachDTO> getAllCoaches(Pageable pageable);

    @GetMapping("/{id}")
    CoachDTO getCoachById(@PathVariable Long id);

    @PostMapping
    CoachDTO createCoach(@RequestBody CoachDTORequest newCoach);

    @PutMapping("/{id}")
    CoachDTO updateCoach(@PathVariable Long id, @RequestBody CoachDTORequest newCoach);

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCoach(@PathVariable Long id) ;
}
