package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.HallControllerImpl;
import fr.hoenheimsports.trainingservice.dto.AddressDto;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.services.SortUtil;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class HallPagedModelAssemblerImpl implements HallPagedModelAssembler{
    private final SortUtil sortUtil;

    public HallPagedModelAssemblerImpl(SortUtil sortUtil) {
        this.sortUtil = sortUtil;
    }

    @Override
    public PagedModel<?> toModel(Page<EntityModel<HallDto>> entity) {

        List<EntityModel<HallDto>> hallModels = entity.getContent();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(
                entity.getSize(), entity.getNumber(), entity.getTotalElements(), entity.getTotalPages());
        List<String> sort = this.sortUtil.createSortParams(entity.getSort());
        PagedModel<?> pagedModel;
        if (hallModels.isEmpty()) {
            EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
            EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(HallDto.class);
            List<EmbeddedWrapper> embedded = Collections.singletonList(wrapper);
            pagedModel = PagedModel.of(embedded,pageMetadata);
        } else {
            pagedModel = PagedModel.of(hallModels, pageMetadata);
        }

        // Add self link
        pagedModel.add(linkTo(methodOn(HallControllerImpl.class).getAllHalls(entity.getNumber(), entity.getSize(),sort)).withSelfRel()
                .andAffordance(afford(methodOn(HallControllerImpl.class).createHall(new HallDto(1L, "name", new AddressDto("street", "city", "postalCode", "country"))))) //skip default
                .andAffordance(afford(methodOn(HallControllerImpl.class).createHall(new HallDto(1L, "name", new AddressDto("street", "city", "postalCode", "country")))))
        );

        // Add next link if there is a next page
        if (entity.hasNext()) {
            pagedModel.add(linkTo(methodOn(HallControllerImpl.class).getAllHalls(entity.getNumber() + 1, entity.getSize(),sort)).withRel("next").expand());
        }

        // Add prev link if there is a previous page
        if (entity.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(HallControllerImpl.class).getAllHalls(entity.getNumber() - 1, entity.getSize(),sort)).withRel("prev").expand());
        }

        // Add first link
        pagedModel.add(linkTo(methodOn(HallControllerImpl.class).getAllHalls(0, entity.getSize(),sort)).withRel("first").expand());

        // Add last link
        int lastPage = entity.getTotalPages() - 1;
        pagedModel.add(linkTo(methodOn(HallControllerImpl.class).getAllHalls(lastPage, entity.getSize(),sort)).withRel("last").expand());

        return pagedModel;
    }
}
