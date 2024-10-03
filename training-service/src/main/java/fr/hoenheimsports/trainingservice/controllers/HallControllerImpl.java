package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.ressources.HallModel;
import fr.hoenheimsports.trainingservice.services.HallServiceImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/halls",
        produces = "application/prs.hal-forms+json")
public class HallControllerImpl implements HallController {

    private final HallServiceImpl hallService;

    public HallControllerImpl(HallServiceImpl hallService) {
        this.hallService = hallService;
    }

    @Override
    @GetMapping
    public PagedModel<HallModel> getAllHalls(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size,
                                             @RequestParam(name = "sort", required = false) List<String> sort) {

        return hallService.getAllHalls(page, size, sort);
    }

    @Override
    @GetMapping("/{id}")
    public HallModel getHallById(@PathVariable Long id) throws HallNotFoundException {
        return hallService.getHallById(id);
    }

    @Override
    @PostMapping
    public HallModel createHall(@RequestBody HallDto newHall) {
        return hallService.createHall(newHall);
    }

    @Override
    @PutMapping("/{id}")
    public HallModel updateHall(@PathVariable Long id, @RequestBody HallDto newHall) throws HallNotFoundException {
        return hallService.updateHall(id, newHall);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }
}