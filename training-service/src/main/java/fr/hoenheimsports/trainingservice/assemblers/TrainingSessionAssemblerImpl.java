package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.TrainingSessionControllerImpl;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDTO;
import fr.hoenheimsports.trainingservice.dto.request.AddressDTORequest;
import fr.hoenheimsports.trainingservice.dto.request.HallDTORequest;
import fr.hoenheimsports.trainingservice.dto.request.TimeSlotDTORequest;
import fr.hoenheimsports.trainingservice.dto.request.TrainingSessionDTORequest;
import fr.hoenheimsports.trainingservice.mappers.TrainingSessionMapper;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import fr.hoenheimsports.trainingservice.services.UserSecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TrainingSessionAssemblerImpl implements TrainingSessionAssembler {

    private final TrainingSessionMapper trainingSessionMapper;
    private final PagedResourcesAssembler<TrainingSession> pagedResourcesAssembler;
    private final UserSecurityService userSecurityService;

    public TrainingSessionAssemblerImpl(TrainingSessionMapper trainingSessionMapper, PagedResourcesAssembler<TrainingSession> pagedResourcesAssembler, UserSecurityService userSecurityService) {
        this.trainingSessionMapper = trainingSessionMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userSecurityService = userSecurityService;
    }

    @NonNull
    @Override
    public TrainingSessionDTO toModel(@NonNull TrainingSession entity) {
        TrainingSessionDTO trainingSessionDTORequest = this.trainingSessionMapper.toDto(entity);
        this.addLinks(trainingSessionDTORequest);
        return trainingSessionDTORequest;
    }

    @NonNull
    @Override
    public CollectionModel<TrainingSessionDTO> toCollectionModel(@NonNull Iterable<? extends TrainingSession> entities) {
        CollectionModel<TrainingSessionDTO> resources =  TrainingSessionAssembler.super.toCollectionModel(entities);
        this.addLinks(resources);
        return resources;
    }

    @Override
    public PagedModel<TrainingSessionDTO> toPagedModel(Page<TrainingSession> page) {
        PagedModel<TrainingSessionDTO> resources = this.pagedResourcesAssembler.toModel(page,this);
        this.addLinks(resources);
        return resources;
    }

    public void addLinks(TrainingSessionDTO resource) {
        resource.add(linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(Pageable.unpaged())).withRel("trainingSessions").expand());
        if(this.userSecurityService.hasRole("ADMIN")) {
            resource.add(
                    linkTo(methodOn(TrainingSessionControllerImpl.class).getTrainingSessionById(resource.getId())).withSelfRel()
                            .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).updateTrainingSession(resource.getId(), null))) //skip default
                            .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).updateTrainingSession(resource.getId(), null)))
                            .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).deleteTrainingSession(resource.getId())))
            );
        } else {
            resource.add(linkTo(methodOn(TrainingSessionControllerImpl.class).getTrainingSessionById(resource.getId())).withSelfRel());

        }
    }

    public void addLinks(CollectionModel<TrainingSessionDTO> resources) {
        if(this.userSecurityService.hasRole("ADMIN")) {
            var trainingSessionDtoRequest = new TrainingSessionDTORequest(1L,new TimeSlotDTORequest(DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now()),new HallDTORequest(1L, "name", new AddressDTORequest("street", "city", "postalCode", "country")));
            resources.add(linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(Pageable.unpaged())).withRel("trainingSessions").expand()
                    .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).createTrainingSession(trainingSessionDtoRequest))) // skip default name
                    .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).createTrainingSession(trainingSessionDtoRequest))));
        } else {
            resources.add(linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(Pageable.unpaged())).withRel("trainingSessions").expand());
        }


    }


}
