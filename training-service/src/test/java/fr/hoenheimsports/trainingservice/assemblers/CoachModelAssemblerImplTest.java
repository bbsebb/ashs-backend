package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.controllers.CoachControllerImpl;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CoachModelAssemblerImplTest {

    private CoachModelAssemblerImpl coachModelAssemblerImpl;

    @BeforeEach
    public void setup() {
        coachModelAssemblerImpl = new CoachModelAssemblerImpl();
    }

    @Test
    public void testToModel() throws CoachNotFoundException {
        // Arrange
        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");

        // Act
        EntityModel<CoachDto> coachModel = coachModelAssemblerImpl.toModel(coachDto);

        // Assert
        assertThat(coachModel).isNotNull();
        assertThat(coachModel.getContent()).isEqualTo(coachDto);
        assertThat(coachModel.getLinks()).hasSize(2);

        Link selfLink = coachModel.getLink("self").orElseThrow();
        String expectedSelfLink = linkTo(methodOn(CoachControllerImpl.class).getCoachById(coachDto.id())).withSelfRel().getHref();
        assertThat(selfLink.getHref()).isEqualTo(expectedSelfLink);

        // Manually construct the expected link without the optional {&sort} parameter
        String expectedCoachesLink = linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(0, 20, null))
                .withRel("coaches")
                .toUri()
                .toString();

        Link coachesLink = coachModel.getLink("coaches").orElseThrow();
        assertThat(coachesLink.getHref()).isEqualTo(expectedCoachesLink);
    }
}
