package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.dto.CoachDto;

import fr.hoenheimsports.trainingservice.ressources.CoachModel;
import fr.hoenheimsports.trainingservice.services.CoachService;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/coaches",
        produces = "application/prs.hal-forms+json")
public class CoachControllerImpl implements CoachController {

    private final CoachService coachService;

    public CoachControllerImpl(CoachService coachService) {
        this.coachService = coachService;
    }

    @Override
    @GetMapping
    public PagedModel<CoachModel> getAllCoaches(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestParam(name = "sort", required = false) List<String> sort) {

        return coachService.getAllCoaches(page, size, sort);
    }


    @Override
    @GetMapping("/{id}")
    public CoachModel getCoachById(@PathVariable Long id) throws CoachNotFoundException {
        return coachService.getCoachById(id);
    }

    @Override
    @PostMapping
    public CoachModel createCoach(@RequestBody CoachDto newCoach) {
        return coachService.createCoach(newCoach);
    }

    @Override
    @PutMapping("/{id}")
    public CoachModel updateCoach(@PathVariable Long id, @RequestBody CoachDto newCoach) throws CoachNotFoundException {
        return coachService.updateCoach(id, newCoach);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoach(@PathVariable Long id) {
        coachService.deleteCoach(id);
        return ResponseEntity.noContent().build();
    }
}