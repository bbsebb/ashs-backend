package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.controllers.HallControllerImpl;
import fr.hoenheimsports.trainingservice.controllers.TrainingSessionControllerImpl;
import fr.hoenheimsports.trainingservice.dto.AddressDto;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.dto.TimeSlotDto;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import fr.hoenheimsports.trainingservice.ressources.TrainingSessionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class TrainingSessionModelAssemblerImplTest {

    private TrainingSessionModelAssemblerImpl trainingSessionModelAssemblerImpl;

    @BeforeEach
    public void setup() {
        trainingSessionModelAssemblerImpl = new TrainingSessionModelAssemblerImpl();
    }

    @Test
    public void testToModel() throws TrainingSessionNotFoundException, HallNotFoundException {
        // Arrange
        AddressDto addressDto = new AddressDto("123 Street", "City", "12345", "Country");
        HallDto hallDto = new HallDto(1L, "Main Hall", addressDto);
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, timeSlotDto, hallDto);

        // Act
        TrainingSessionModel trainingSessionModel = trainingSessionModelAssemblerImpl.toModel(trainingSessionDto);

        // Assert
        assertThat(trainingSessionModel).isNotNull();
        assertThat(trainingSessionModel.getContent()).isEqualTo(trainingSessionDto);
        assertThat(trainingSessionModel.getLinks()).hasSize(3);

        Link selfLink = trainingSessionModel.getLink("self").orElseThrow();
        String expectedSelfLink = linkTo(methodOn(TrainingSessionControllerImpl.class).getTrainingSessionById(trainingSessionDto.id())).withSelfRel().getHref();
        assertThat(selfLink.getHref()).isEqualTo(expectedSelfLink);

        Link trainingSessionsLink = trainingSessionModel.getLink("trainingSessions").orElseThrow();
        String expectedTrainingSessionsLink = linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(0, 20, null)).withRel("trainingSessions").toUri().toString();
        assertThat(trainingSessionsLink.getHref()).isEqualTo(expectedTrainingSessionsLink);

        Link hallLink = trainingSessionModel.getLink("hall").orElseThrow();
        String expectedHallLink = linkTo(methodOn(HallControllerImpl.class).getHallById(hallDto.id())).withRel("hall").getHref();
        assertThat(hallLink.getHref()).isEqualTo(expectedHallLink);
    }
}