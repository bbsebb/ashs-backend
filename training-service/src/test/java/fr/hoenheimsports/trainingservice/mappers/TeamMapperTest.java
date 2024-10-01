package fr.hoenheimsports.trainingservice.mappers;
import fr.hoenheimsports.trainingservice.dto.*;
import fr.hoenheimsports.trainingservice.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class TeamMapperTest {

    @Mock
    private CoachMapper coachMapper;


    @Mock
    private TrainingSessionMapper trainingSessionMapper;


    @InjectMocks
    private TeamMapperImpl teamMapper;

    private Coach coach;
    private TrainingSession trainingSession;

    @BeforeEach
    public void setup() {
        // Initialisation des objets communs
        coach = Coach.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        TimeSlot timeSlot = new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));

        Hall hall = Hall.builder()
                .id(1L)
                .name("Main Hall")
                .address(new Address("123 Street", "City", "12345", "Country"))
                .build();

        trainingSession = TrainingSession.builder()
                .id(1L)
                .timeSlot(timeSlot)
                .hall(hall)
                .build();
    }

    @Test
    public void testToEntity() {
        // Arrange
        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, timeSlotDto, hallDto);
        TeamDto teamDto = new TeamDto(1L, Gender.M, Category.U15, 1, coachDto, Set.of(trainingSessionDto));

        // BDDMockito - Given
        given(coachMapper.toEntity(coachDto)).willReturn(coach);
        given(trainingSessionMapper.toEntity(trainingSessionDto)).willReturn(trainingSession);

        // Act - When
        Team team = teamMapper.toEntity(teamDto);

        // Assert - Then
        assertThat(team).isNotNull();
        assertThat(team.getId()).isEqualTo(teamDto.id());
        assertThat(team.getGender()).isEqualTo(teamDto.gender());
        assertThat(team.getCategory()).isEqualTo(teamDto.category());
        assertThat(team.getTeamNumber()).isEqualTo(teamDto.teamNumber());
        assertThat(team.getCoach().getId()).isEqualTo(coachDto.id());
        assertThat(team.getTrainingSessions()).hasSize(1);
    }

    @Test
    public void testToDto() {
        // Arrange
        Team team = Team.builder()
                .id(1L)
                .gender(Gender.M)
                .category(Category.U15)
                .teamNumber(1)
                .coach(coach)
                .trainingSessions(Set.of(trainingSession))
                .build();

        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, timeSlotDto, hallDto);

        // BDDMockito - Given
        given(coachMapper.toDto(coach)).willReturn(coachDto);
        given(trainingSessionMapper.toDto(trainingSession)).willReturn(trainingSessionDto);

        // Act - When
        TeamDto teamDto = teamMapper.toDto(team);

        // Assert - Then
        assertThat(teamDto).isNotNull();
        assertThat(teamDto.id()).isEqualTo(team.getId());
        assertThat(teamDto.gender()).isEqualTo(team.getGender());
        assertThat(teamDto.category()).isEqualTo(team.getCategory());
        assertThat(teamDto.teamNumber()).isEqualTo(team.getTeamNumber());
        assertThat(teamDto.coach().id()).isEqualTo(coach.getId());
        assertThat(teamDto.trainingSessions()).hasSize(1);
    }

    @Test
    public void testPartialUpdate() {
        // Arrange
        CoachDto coachDto = new CoachDto(null, "John", null, "john.doe@example.com", null);
        TeamDto teamDto = new TeamDto(null, Gender.M, null, 1, coachDto, null);

        Coach existingCoach = Coach.builder()
                .id(1L)
                .name("Jane")
                .surname("Smith")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .build();

        Team team = new Team();
        team.setId(1L);
        team.setGender(Gender.F);
        team.setCategory(Category.U13);
        team.setTeamNumber(2);
        team.setCoach(existingCoach);
        team.setTrainingSessions(Set.of(trainingSession));

        // Mock le comportement de coachMapper.partialUpdate(...) pour appliquer les modifications
        willAnswer(invocation -> {
            CoachDto dto = invocation.getArgument(0);
            Coach coach = invocation.getArgument(1);

            if (dto.name() != null) {
                coach.setName(dto.name());
            }
            if (dto.email() != null) {
                coach.setEmail(dto.email());
            }
            return coach;
        }).given(coachMapper).partialUpdate(coachDto, existingCoach);

        // Act
        teamMapper.partialUpdate(teamDto, team);

        // Assert
        assertThat(team).isNotNull();
        assertThat(team.getId()).isEqualTo(1L); // id should not be updated
        assertThat(team.getGender()).isEqualTo(teamDto.gender()); // gender should be updated
        assertThat(team.getCategory()).isEqualTo(Category.U13); // category should not be updated
        assertThat(team.getTeamNumber()).isEqualTo(teamDto.teamNumber()); // teamNumber should be updated
        assertThat(team.getCoach().getName()).isEqualTo("John"); // coach name should be updated
        assertThat(team.getCoach().getSurname()).isEqualTo("Smith"); // coach surname should not be updated
        assertThat(team.getCoach().getEmail()).isEqualTo("john.doe@example.com"); // coach email should be updated
        assertThat(team.getCoach().getPhone()).isEqualTo("0987654321"); // coach phone should not be updated
        assertThat(team.getTrainingSessions()).hasSize(1); // training sessions should remain the same
    }

}
