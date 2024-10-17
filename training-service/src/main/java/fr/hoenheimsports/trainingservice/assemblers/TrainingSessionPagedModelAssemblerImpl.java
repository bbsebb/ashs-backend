package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.controllers.TrainingSessionControllerImpl;
import fr.hoenheimsports.trainingservice.dto.AddressDto;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.dto.TimeSlotDto;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import fr.hoenheimsports.trainingservice.services.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TrainingSessionPagedModelAssemblerImpl implements TrainingSessionPagedModelAssembler{
    private final SortUtil sortUtil;

    public TrainingSessionPagedModelAssemblerImpl(SortUtil sortUtil) {
        this.sortUtil = sortUtil;
    }

    @Override
    public PagedModel<?> toModel(Page<EntityModel<TrainingSessionDto>> entity) {

        List<EntityModel<TrainingSessionDto>> trainingSessionModels = entity.getContent();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                entity.getSize(), entity.getNumber(), entity.getTotalElements(), entity.getTotalPages());

        List<String> sort = this.sortUtil.createSortParams(entity.getSort());

        PagedModel<?> pagedModel;
        if (trainingSessionModels.isEmpty()) {
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(TrainingSessionDto.class);
            List<EmbeddedWrapper> embedded = Collections.singletonList(wrapper);
            pagedModel = PagedModel.of(embedded,pageMetadata);
        } else {
            pagedModel = PagedModel.of(trainingSessionModels, pageMetadata);
        }

        var trainingSessionDto = new TrainingSessionDto(1L,new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.now(), LocalTime.now()),new HallDto(1L, "name", new AddressDto("street", "city", "postalCode", "country")));
        // Add self link

        pagedModel.add(linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(entity.getNumber(), entity.getSize(), sort)).withSelfRel()
                .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).createTrainingSession(trainingSessionDto))) // skip default name
                .andAffordance(afford(methodOn(TrainingSessionControllerImpl.class).createTrainingSession(trainingSessionDto)))
        );


        // Add next link if there is a next page
        if (entity.hasNext()) {
            pagedModel.add(linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(entity.getNumber() + 1, entity.getSize(), sort)).withRel("next").expand());
        }

        // Add prev link if there is a previous page
        if (entity.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(entity.getNumber() - 1, entity.getSize(), sort)).withRel("prev").expand());
        }

        // Add first link
        pagedModel.add(linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(0, entity.getSize(), sort)).withRel("first").expand());

        // Add last link
        int lastPage = entity.getTotalPages() - 1;
        pagedModel.add(linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(lastPage, entity.getSize(), sort)).withRel("last").expand());

        return pagedModel;
    }
}