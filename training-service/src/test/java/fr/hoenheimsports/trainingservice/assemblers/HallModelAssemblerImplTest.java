package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.controllers.HallControllerImpl;
import fr.hoenheimsports.trainingservice.dto.AddressDto;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HallModelAssemblerImplTest {

    private HallModelAssemblerImpl hallModelAssemblerImpl;

    @BeforeEach
    public void setup() {
        hallModelAssemblerImpl = new HallModelAssemblerImpl();
    }

    @Test
    public void testToModel() throws HallNotFoundException {
        // Arrange
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("1", "Rue de la République", "Hoenheim", "67800"));

        // Act
        EntityModel<HallDto> hallModel = hallModelAssemblerImpl.toModel(hallDto);

        // Assert
        assertThat(hallModel).isNotNull();
        assertThat(hallModel.getContent()).isEqualTo(hallDto);
        assertThat(hallModel.getLinks()).hasSize(2);

        Link selfLink = hallModel.getLink("self").orElseThrow();
        String expectedSelfLink = linkTo(methodOn(HallControllerImpl.class).getHallById(hallDto.id())).withSelfRel().getHref();
        assertThat(selfLink.getHref()).isEqualTo(expectedSelfLink);

        String expectedHallsLink = linkTo(methodOn(HallControllerImpl.class).getAllHalls(0, 20, null))
                .withRel("halls")
                .toUri()
                .toString();

        Link hallsLink = hallModel.getLink("halls").orElseThrow();
        assertThat(hallsLink.getHref()).isEqualTo(expectedHallsLink);
    }
}