package fr.hoenheimsports.trainingservice.repositories;

import fr.hoenheimsports.trainingservice.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CoachRepository coachRepository;

    private Team team;

    @BeforeEach
    public void setup() {
        // Création du coach
        Coach coach = Coach.builder()
                .name("Jane")
                .surname("Smith")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .build();
        coach = coachRepository.save(coach); // Sauvegarder le coach pour l'utiliser dans les tests

        // Création de sessions d'entraînement fictives (vides pour l'instant)
        Set<TrainingSession> trainingSessions = new HashSet<>();

        // Création de l'équipe
        team = Team.builder()
                .gender(Gender.F)
                .category(Category.U18)
                .teamNumber(1)
                .coach(coach)
                .trainingSessions(trainingSessions)
                .build();
    }

    @Test
    public void testSaveTeam() {
        // Act
        Team savedTeam = teamRepository.save(team);

        // Assert
        assertThat(savedTeam).isNotNull();
        assertThat(savedTeam.getId()).isNotNull();
        assertThat(savedTeam.getGender()).isEqualTo(team.getGender());
        assertThat(savedTeam.getCategory()).isEqualTo(team.getCategory());
        assertThat(savedTeam.getTeamNumber()).isEqualTo(team.getTeamNumber());
        assertThat(savedTeam.getCoach()).isEqualTo(team.getCoach());
        assertThat(savedTeam.getTrainingSessions()).isEqualTo(team.getTrainingSessions());
    }

    @Test
    public void testFindById() {
        // Arrange
        Team savedTeam = teamRepository.save(team);

        // Act
        Optional<Team> foundTeam = teamRepository.findById(savedTeam.getId());

        // Assert
        assertThat(foundTeam).isNotEmpty();
        assertThat(foundTeam.get()).isEqualTo(savedTeam);
    }

    @Test
    public void testFindAll() {
        // Arrange
        teamRepository.save(team);

        // Act
        List<Team> teams = teamRepository.findAll();

        // Assert
        assertThat(teams).isNotEmpty();
        assertThat(teams).contains(team);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        Team savedTeam = teamRepository.save(team);

        // Act
        teamRepository.deleteById(savedTeam.getId());
        Optional<Team> foundTeam = teamRepository.findById(savedTeam.getId());

        // Assert
        assertThat(foundTeam).isEmpty();
    }
}
