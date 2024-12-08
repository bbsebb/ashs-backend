package fr.hoenheimsports.trainingservice.controllers;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.dto.HallDTO;
import fr.hoenheimsports.trainingservice.dto.request.HallDTORequest;
import fr.hoenheimsports.trainingservice.services.HallServiceImpl;
import fr.hoenheimsports.trainingservice.validators.OnCreate;
import fr.hoenheimsports.trainingservice.validators.OnUpdate;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public PagedModel<HallDTO> getAllHalls(@ParameterObject Pageable pageable) {

        return hallService.getAllModels(pageable);
    }

    @Override
    @GetMapping("/{id}")
    public HallDTO getHallById(@PathVariable Long id) throws HallNotFoundException {
        return hallService.getModelById(id);
    }

    @Override
    @PostMapping
    public HallDTO createHall(@Validated(OnCreate.class)  @RequestBody HallDTORequest newHall) {
        return hallService.createAndConvertToModel(newHall);
    }

    @Override
    @PutMapping("/{id}")
    public HallDTO updateHall(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody HallDTORequest newHall) throws HallNotFoundException {
        return hallService.updateAndConvertToModel(id, newHall);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHall(@PathVariable Long id) {
        hallService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}