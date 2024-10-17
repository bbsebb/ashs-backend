package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TeamNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.TeamModelAssembler;
import fr.hoenheimsports.trainingservice.assemblers.TeamPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.TeamDto;
import fr.hoenheimsports.trainingservice.mappers.TeamMapper;
import fr.hoenheimsports.trainingservice.models.*;
import fr.hoenheimsports.trainingservice.repositories.CoachRepository;
import fr.hoenheimsports.trainingservice.repositories.TeamRepository;
import fr.hoenheimsports.trainingservice.repositories.TrainingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    //@Mock
    //private CoachRepository coachRepository;

    //@Mock
    //private TrainingSessionRepository trainingSessionRepository;

    @Mock
    private TeamModelAssembler teamModelAssembler;

    @Mock
    private TeamPagedModelAssembler teamPagedModelAssembler;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private SortUtil sortUtil;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    void createTeam_ValidTeamDto_ReturnsTeamModel()   {
        TeamDto teamDto = new TeamDto(null, Gender.M, Category.U15, 1, null, null);
        Team team = Team.builder()
                .gender(Gender.M)
                .category(Category.U15)
                .teamNumber(1)
                .build();
        Team savedTeam = Team.builder()
                .id(1L)
                .gender(Gender.M)
                .category(Category.U15)
                .teamNumber(1)
                .build();
        EntityModel<TeamDto> teamModel = EntityModel.of(new TeamDto(1L, Gender.M, Category.U15, 1, null, null));

        given(teamRepository.save(any(Team.class))).willReturn(savedTeam);
        // Créer un Answer personnalisé qui retourne simplement l'objet qu'il reçoit
/*        given(trainingSessionRepository.save(any(TrainingSession.class))).willAnswer(invocation -> {
            // Simuler une action sur chaque créneau si nécessaire
            return invocation.<TrainingSession>getArgument(0); // Retourner la session comme si elle avait été sauvegardée
        });*/
/*        given(coachRepository.save(any(Coach.class))).willReturn(savedTeam.getCoach());*/
        given(teamMapper.toEntity(teamDto)).willReturn(team);
        given(teamMapper.toDto(savedTeam)).willReturn(new TeamDto(1L, Gender.M, Category.U15, 1, null, null));
        given(teamModelAssembler.toModel(any(TeamDto.class))).willReturn(teamModel);

        EntityModel<TeamDto> createdTeam = teamService.createTeam(teamDto);

        assertThat(createdTeam).isNotNull();
        assertThat(Objects.requireNonNull(createdTeam.getContent()).id()).isEqualTo(1L);
        then(teamRepository).should(times(1)).save(any(Team.class));
    }

    @Test
    void getAllTeams_ValidPageAndSize_ReturnsPagedModel() {
        List<String> sortParams = List.of("teamNumber,asc");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("teamNumber").ascending());
        List<Team> teams = Arrays.asList(
                Team.builder()
                        .id(1L)
                        .gender(Gender.M)
                        .category(Category.U15)
                        .teamNumber(1)
                        .build(),
                Team.builder()
                        .id(2L)
                        .gender(Gender.F)
                        .category(Category.U13)
                        .teamNumber(2)
                        .build()
        );
        Page<Team> teamPage = new PageImpl<>(teams, pageable, teams.size());
        List<TeamDto> teamDtos = Arrays.asList(
                new TeamDto(1L, Gender.M, Category.U15, 1, null, null),
                new TeamDto(2L, Gender.F, Category.U13, 2, null, null)
        );
        List<EntityModel<TeamDto>> teamModels = Arrays.asList(
                EntityModel.of(teamDtos.get(0)),
                EntityModel.of(teamDtos.get(1))
        );
        PagedModel<EntityModel<TeamDto>> pagedModel = PagedModel.of(teamModels, new PagedModel.PageMetadata(10, 0, teamModels.size()));

        given(sortUtil.createSort(sortParams)).willReturn(pageable.getSort());
        given(teamRepository.findAll(any(Pageable.class))).willReturn(teamPage);
        given(teamMapper.toDto(any(Team.class))).willReturn(teamDtos.get(0), teamDtos.get(1));
        given(teamPagedModelAssembler.toModel(ArgumentMatchers.any())).willAnswer(invocation -> {
            // Accès à l'argument Page<CoachModel> si nécessaire
            Page<EntityModel<TeamDto>> page = invocation.getArgument(0);

            // Vous pouvez effectuer des opérations sur cet argument si nécessaire
            // et ensuite retourner l'objet simulé
            return pagedModel; // Retourne pagedModel
        });

        PagedModel<EntityModel<TeamDto>> result = (PagedModel<EntityModel<TeamDto>>) teamService.getAllTeams(0, 10, sortParams);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2).containsExactlyInAnyOrderElementsOf(teamModels);
        then(teamRepository).should(times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getTeamById_TeamNotFoundException() {
        given(teamRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.getTeamById(1L))
                .isInstanceOf(TeamNotFoundException.class);

        then(teamRepository).should(times(1)).findById(1L);
    }

    @Test
    void getTeamById_ValidId_ReturnsTeamModel() throws TeamNotFoundException {
        Team team = Team.builder()
                .id(1L)
                .gender(Gender.M)
                .category(Category.U15)
                .teamNumber(1)
                .build();
        TeamDto teamDto = new TeamDto(1L, Gender.M, Category.U15, 1, null, null);
        EntityModel<TeamDto> teamModel = EntityModel.of(teamDto);

        given(teamRepository.findById(1L)).willReturn(Optional.of(team));
        given(teamMapper.toDto(team)).willReturn(teamDto);
        given(teamModelAssembler.toModel(teamDto)).willReturn(teamModel);

        EntityModel<TeamDto> result = teamService.getTeamById(1L);

        assertThat(result).isNotNull();
        assertThat(Objects.requireNonNull(result.getContent()).id()).isEqualTo(1L);
        then(teamRepository).should(times(1)).findById(1L);
    }

    @Test
    void updateTeam_TeamNotFoundException() {
        TeamDto teamDto = new TeamDto(null, Gender.M, Category.U15, 1, null, null);
        given(teamRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> teamService.updateTeam(1L, teamDto))
                .isInstanceOf(TeamNotFoundException.class);

        then(teamRepository).should(times(1)).findById(1L);
        then(teamRepository).should(never()).save(any(Team.class));
    }

    @Test
    void updateTeam_ValidIdAndTeamDto_ReturnsUpdatedTeamModel() throws TeamNotFoundException {
        TeamDto teamDto = new TeamDto(null, Gender.M, Category.U15, 1, null, null);
        Team team = Team.builder()
                .id(1L)
                .gender(Gender.M)
                .category(Category.U15)
                .teamNumber(1)
                .build();
        Team updatedTeam = Team.builder()
                .id(1L)
                .gender(Gender.M)
                .category(Category.U15)
                .teamNumber(1)
                .build();
        TeamDto updatedTeamDto = new TeamDto(1L, Gender.M, Category.U15, 1, null, null);
        EntityModel<TeamDto> updatedTeamModel = EntityModel.of(updatedTeamDto);

        given(teamRepository.findById(1L)).willReturn(Optional.of(team));
        given(teamMapper.partialUpdate(teamDto, team)).willReturn(updatedTeam);
        given(teamRepository.save(any(Team.class))).willReturn(updatedTeam);
        given(teamMapper.toDto(updatedTeam)).willReturn(updatedTeamDto);
        given(teamModelAssembler.toModel(updatedTeamDto)).willReturn(updatedTeamModel);

        EntityModel<TeamDto> result = teamService.updateTeam(1L, teamDto);

        assertThat(result).isNotNull();
        assertThat(Objects.requireNonNull(result.getContent()).id()).isEqualTo(1L);
        then(teamRepository).should(times(1)).findById(1L);
        then(teamRepository).should(times(1)).save(any(Team.class));
    }

    @Test
    void deleteTeam_ValidId_DeletesTeam() {
        willDoNothing().given(teamRepository).deleteById(1L);

        teamService.deleteTeam(1L);

        then(teamRepository).should(times(1)).deleteById(1L);
    }
}