package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.TeamControllerImpl;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import fr.hoenheimsports.trainingservice.models.Category;
import fr.hoenheimsports.trainingservice.models.Gender;
import fr.hoenheimsports.trainingservice.ressources.TeamModel;
import fr.hoenheimsports.trainingservice.services.SortUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class TeamPagedModelAssemblerImplTest {
    @InjectMocks
    private TeamPagedModelAssemblerImpl teamPagedModelAssemblerImpl;

    @Mock
    private SortUtil sortUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToModelFirstPage() {
        // Arrange
        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, null, null);
        TeamDto teamDto1 = new TeamDto(1L, Gender.M, Category.SENIOR, 1, coachDto, Set.of(trainingSessionDto));
        TeamDto teamDto2 = new TeamDto(2L, Gender.F, Category.U11, 2, coachDto, Set.of(trainingSessionDto));

        TeamModel teamModel1 = new TeamModel(teamDto1);
        TeamModel teamModel2 = new TeamModel(teamDto2);

        List<TeamModel> teamModels = List.of(teamModel1, teamModel2);
        Page<TeamModel> page = new PageImpl<>(teamModels, PageRequest.of(0, 2, Sort.by("name")), 4);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<TeamModel> pagedModel = teamPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(teamModel1, teamModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(4);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(0);

        String baseUrl = linkTo(methodOn(TeamControllerImpl.class).getAllTeams(0, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(4);

        // Utilisation sécurisée de Optional avec ifPresent()
        pagedModel.getLink("self").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        pagedModel.getLink("next").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=0", "page=1"))
        );

        assertThat(pagedModel.getLink("prev")).isNotPresent();

        pagedModel.getLink("first").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        pagedModel.getLink("last").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=0", "page=1"))
        );
    }

    @Test
    public void testToModelLastPage() {
        // Arrange
        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, null, null);
        TeamDto teamDto1 = new TeamDto(3L, Gender.M, Category.SENIOR, 1, coachDto, Set.of(trainingSessionDto));
        TeamDto teamDto2 = new TeamDto(4L, Gender.F, Category.U11, 2, coachDto, Set.of(trainingSessionDto));

        TeamModel teamModel1 = new TeamModel(teamDto1);
        TeamModel teamModel2 = new TeamModel(teamDto2);

        List<TeamModel> teamModels = List.of(teamModel1, teamModel2);
        Page<TeamModel> page = new PageImpl<>(teamModels, PageRequest.of(1, 2, Sort.by("name")), 4);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<TeamModel> pagedModel = teamPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(teamModel1, teamModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(4);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(1);

        String baseUrl = linkTo(methodOn(TeamControllerImpl.class).getAllTeams(1, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(4);

        pagedModel.getLink("self").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        assertThat(pagedModel.getLink("next")).isNotPresent();

        pagedModel.getLink("prev").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=0"))
        );

        pagedModel.getLink("first").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=0"))
        );

        pagedModel.getLink("last").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );
    }
    @Test
    public void testToModelSinglePage() {
        // Arrange
        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, null, null);
        TeamDto teamDto1 = new TeamDto(1L, Gender.M, Category.SENIOR, 1, coachDto, Set.of(trainingSessionDto));
        TeamDto teamDto2 = new TeamDto(2L, Gender.F, Category.U11, 2, coachDto, Set.of(trainingSessionDto));

        TeamModel teamModel1 = new TeamModel(teamDto1);
        TeamModel teamModel2 = new TeamModel(teamDto2);

        List<TeamModel> teamModels = List.of(teamModel1, teamModel2);
        Page<TeamModel> page = new PageImpl<>(teamModels, PageRequest.of(0, 2, Sort.by("name")), 2);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<TeamModel> pagedModel = teamPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(teamModel1, teamModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(1);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(0);

        String baseUrl = linkTo(methodOn(TeamControllerImpl.class).getAllTeams(0, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(3);

        // Utilisation sécurisée de Optional avec ifPresent()
        pagedModel.getLink("self").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        assertThat(pagedModel.getLink("next")).isNotPresent();
        assertThat(pagedModel.getLink("prev")).isNotPresent();

        pagedModel.getLink("first").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        pagedModel.getLink("last").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );
    }

    @Test
    public void testToModelMiddlePage() {
        // Arrange
        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, null, null);
        TeamDto teamDto1 = new TeamDto(1L, Gender.M, Category.SENIOR, 1, coachDto, Set.of(trainingSessionDto));
        TeamDto teamDto2 = new TeamDto(2L, Gender.F, Category.U11, 2, coachDto, Set.of(trainingSessionDto));

        TeamModel teamModel1 = new TeamModel(teamDto1);
        TeamModel teamModel2 = new TeamModel(teamDto2);

        List<TeamModel> teamModels = List.of(teamModel1, teamModel2);
        Page<TeamModel> page = new PageImpl<>(teamModels, PageRequest.of(1, 2, Sort.by("name")), 6);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<TeamModel> pagedModel = teamPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(teamModel1, teamModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(6);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(3);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(1);

        String baseUrl = linkTo(methodOn(TeamControllerImpl.class).getAllTeams(1, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(5);

        // Utilisation sécurisée de Optional avec ifPresent()
        pagedModel.getLink("self").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        pagedModel.getLink("next").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=2"))
        );

        pagedModel.getLink("prev").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=0"))
        );

        pagedModel.getLink("first").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=0"))
        );

        pagedModel.getLink("last").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=2"))
        );
    }
}