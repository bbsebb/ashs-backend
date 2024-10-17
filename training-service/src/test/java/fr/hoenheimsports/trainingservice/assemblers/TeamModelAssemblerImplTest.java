package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.controllers.CoachControllerImpl;
import fr.hoenheimsports.trainingservice.controllers.TeamControllerImpl;
import fr.hoenheimsports.trainingservice.controllers.TrainingSessionControllerImpl;
import fr.hoenheimsports.trainingservice.dto.*;
import fr.hoenheimsports.trainingservice.models.Category;
import fr.hoenheimsports.trainingservice.models.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class TeamModelAssemblerImplTest {

    private TeamModelAssemblerImpl teamModelAssemblerImpl;

    @BeforeEach
    public void setup() {
        teamModelAssemblerImpl = new TeamModelAssemblerImpl();
    }

    @Test
    public void testToModel() throws TeamNotFoundException, CoachNotFoundException, TrainingSessionNotFoundException {
        // Arrange
        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.now(),LocalTime.now().plusHours(1));
        HallDto hallDto = new HallDto(1L, "Hall A",new AddressDto("Street", "City", "ZipCode","France"));
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, timeSlotDto, hallDto);
        TeamDto teamDto = new TeamDto(1L, Gender.N, Category.SENIOR, 1, coachDto, Set.of(trainingSessionDto));

        // Act
        EntityModel<TeamDto> teamModel = teamModelAssemblerImpl.toModel(teamDto);

        // Assert
        assertThat(teamModel).isNotNull();
        assertThat(teamModel.getContent()).isEqualTo(teamDto);
        assertThat(teamModel.getLinks()).hasSize(4);

        Link selfLink = teamModel.getLink("self").orElseThrow();
        String expectedSelfLink = linkTo(methodOn(TeamControllerImpl.class).getTeamById(teamDto.id())).withSelfRel().getHref();
        assertThat(selfLink.getHref()).isEqualTo(expectedSelfLink);

        Link teamsLink = teamModel.getLink("teams").orElseThrow();
        String expectedTeamsLink = linkTo(methodOn(TeamControllerImpl.class).getAllTeams(0, 20, null)).withRel("teams").toUri().toString();
        assertThat(teamsLink.getHref()).isEqualTo(expectedTeamsLink);

        Link coachLink = teamModel.getLink("coach").orElseThrow();
        String expectedCoachLink = linkTo(methodOn(CoachControllerImpl.class).getCoachById(coachDto.id())).withRel("coach").getHref();
        assertThat(coachLink.getHref()).isEqualTo(expectedCoachLink);

        Link trainingSessionsLink = teamModel.getLink("trainingSessions").orElseThrow();
        String expectedTrainingSessionsLink = linkTo(methodOn(TrainingSessionControllerImpl.class).getTrainingSessionById(trainingSessionDto.id())).withRel("trainingSessions").getHref();
        assertThat(trainingSessionsLink.getHref()).isEqualTo(expectedTrainingSessionsLink);
    }
}