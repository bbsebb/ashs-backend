package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.HallControllerImpl;
import fr.hoenheimsports.trainingservice.dto.HallDTO;
import fr.hoenheimsports.trainingservice.dto.request.AddressDTORequest;
import fr.hoenheimsports.trainingservice.dto.request.HallDTORequest;
import fr.hoenheimsports.trainingservice.mappers.HallMapper;
import fr.hoenheimsports.trainingservice.models.Hall;
import fr.hoenheimsports.trainingservice.services.UserSecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HallAssemblerImpl implements HallAssembler {
    private final HallMapper hallMapper;
    private final PagedResourcesAssembler<Hall> pagedResourcesAssembler;
    private final UserSecurityService userSecurityService;

    public HallAssemblerImpl(HallMapper hallMapper, PagedResourcesAssembler<Hall> pagedResourcesAssembler, UserSecurityService userSecurityService) {
        this.hallMapper = hallMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userSecurityService = userSecurityService;
    }

    @NonNull
    @Override
    public HallDTO toModel(@NonNull Hall entity) {
        HallDTO HallDto = this.hallMapper.toDto(entity);
        this.addLinks(HallDto);
        return HallDto;
    }

    @NonNull
    @Override
    public CollectionModel<HallDTO> toCollectionModel(@NonNull Iterable<? extends Hall> entities) {
        var resources =  HallAssembler.super.toCollectionModel(entities);
        this.addLinks(resources);
        return resources;
    }

    @Override
    public PagedModel<HallDTO> toPagedModel(Page<Hall> page) {
        PagedModel<HallDTO> resources = this.pagedResourcesAssembler.toModel(page,this);
        this.addLinks(resources);
        return resources;
    }

    private void addLinks(CollectionModel<HallDTO> resources) {
        resources.add(linkTo(methodOn(HallControllerImpl.class).getAllHalls(Pageable.unpaged())).withRel("halls").expand());
        if(this.userSecurityService.hasRole("ADMIN")) {
            resources.add(linkTo(methodOn(HallControllerImpl.class).getAllHalls(Pageable.unpaged())).withRel("halls").expand()
                    .andAffordance(afford(methodOn(HallControllerImpl.class).createHall(new HallDTORequest(1L, "name", new AddressDTORequest("street", "city", "postalCode", "country"))))) //skip default
                    .andAffordance(afford(methodOn(HallControllerImpl.class).createHall(new HallDTORequest(1L, "name", new AddressDTORequest("street", "city", "postalCode", "country")))))
            );
        }

    }

    private void addLinks(HallDTO resource) {
        resource.add(linkTo(methodOn(HallControllerImpl.class).getHallById(resource.getId())).withSelfRel());
        resource.add(linkTo(methodOn(HallControllerImpl.class).getAllHalls(Pageable.unpaged())).withRel("halls").expand());
        if(this.userSecurityService.hasRole("ADMIN")) {
            resource.add(linkTo(methodOn(HallControllerImpl.class).getHallById(resource.getId())).withSelfRel()
                    .andAffordance(afford(methodOn(HallControllerImpl.class).updateHall(resource.getId(), null))) //skip default
                    .andAffordance(afford(methodOn(HallControllerImpl.class).updateHall(resource.getId(), null)))
                    .andAffordance(afford(methodOn(HallControllerImpl.class).deleteHall(resource.getId()))));
        }

    }
}
