package fr.hoenheimsports.trainingservice.repositories;

import fr.hoenheimsports.trainingservice.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TrainingSessionRepositoryTest {

    @Autowired
    private TrainingSessionRepository trainingSessionRepository;

    @Autowired
    private HallRepository hallRepository;

    private TrainingSession trainingSession;

    @BeforeEach
    public void setup() {
        // Création de la salle (Hall)
        Hall hall = Hall.builder()
                .name("Gymnasium")
                .address(Address.builder()
                        .street("123 Rue de la Paix")
                        .city("Strasbourg")
                        .postalCode("67000")
                        .country("France")
                        .build())
                .build();
        hall = hallRepository.save(hall); // Sauvegarder la salle pour l'utiliser dans la session

        // Création du créneau horaire (TimeSlot)
// Création du créneau horaire (TimeSlot)
        TimeSlot timeSlot = new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0));

        // Création de la session d'entraînement
        trainingSession = TrainingSession.builder()
                .timeSlot(timeSlot)
                .hall(hall)
                .build();
    }

    @Test
    public void testSaveTrainingSession() {
        // Act
        TrainingSession savedSession = trainingSessionRepository.save(trainingSession);

        // Assert
        assertThat(savedSession).isNotNull();
        assertThat(savedSession.getId()).isNotNull();
        assertThat(savedSession.getTimeSlot()).isEqualTo(trainingSession.getTimeSlot());
        assertThat(savedSession.getHall()).isEqualTo(trainingSession.getHall());
    }

    @Test
    public void testFindById() {
        // Arrange
        TrainingSession savedSession = trainingSessionRepository.save(trainingSession);

        // Act
        Optional<TrainingSession> foundSession = trainingSessionRepository.findById(savedSession.getId());

        // Assert
        assertThat(foundSession).isNotEmpty();
        assertThat(foundSession.get()).isEqualTo(savedSession);
    }

    @Test
    public void testFindAll() {
        // Arrange
        trainingSessionRepository.save(trainingSession);

        // Act
        List<TrainingSession> sessions = trainingSessionRepository.findAll();

        // Assert
        assertThat(sessions).isNotEmpty();
        assertThat(sessions).contains(trainingSession);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        TrainingSession savedSession = trainingSessionRepository.save(trainingSession);

        // Act
        trainingSessionRepository.deleteById(savedSession.getId());
        Optional<TrainingSession> foundSession = trainingSessionRepository.findById(savedSession.getId());

        // Assert
        assertThat(foundSession).isEmpty();
    }
}
