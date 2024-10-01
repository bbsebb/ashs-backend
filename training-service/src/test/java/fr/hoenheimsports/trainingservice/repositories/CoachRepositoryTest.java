package fr.hoenheimsports.trainingservice.repositories;

import fr.hoenheimsports.trainingservice.models.Coach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CoachRepositoryTest {

    @Autowired
    private CoachRepository coachRepository;

    private Coach coach;

    @BeforeEach
    public void setup() {
        // Arrange
        coach = new Coach();
        coach.setName("John");
        coach.setSurname("Doe");
        coach.setEmail("john.doe@example.com");
        coach.setPhone("1234567890");
    }

    @Test
    public void testSaveCoach() {
        // Act
        Coach savedCoach = coachRepository.save(coach);

        // Assert
        assertThat(savedCoach).isNotNull();
        assertThat(savedCoach.getId()).isNotNull();
        assertThat(savedCoach.getName()).isEqualTo(coach.getName());
        assertThat(savedCoach.getSurname()).isEqualTo(coach.getSurname());
        assertThat(savedCoach.getEmail()).isEqualTo(coach.getEmail());
        assertThat(savedCoach.getPhone()).isEqualTo(coach.getPhone());
    }

    @Test
    public void testFindById() {
        // Arrange
        Coach savedCoach = coachRepository.save(coach);

        // Act
        Optional<Coach> foundCoach = coachRepository.findById(savedCoach.getId());

        // Assert
        assertThat(foundCoach).isNotEmpty();
        assertThat(foundCoach.get()).isEqualTo(savedCoach);
    }

    @Test
    public void testFindAll() {
        // Arrange
        coachRepository.save(coach);

        // Act
        List<Coach> coaches = coachRepository.findAll();

        // Assert
        assertThat(coaches).isNotEmpty();
        assertThat(coaches).contains(coach);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        Coach savedCoach = coachRepository.save(coach);

        // Act
        coachRepository.deleteById(savedCoach.getId());
        Optional<Coach> foundCoach = coachRepository.findById(savedCoach.getId());

        // Assert
        assertThat(foundCoach).isEmpty();
    }
}