package fr.hoenheimsports.trainingservice.repositories;

import fr.hoenheimsports.trainingservice.models.Address;
import fr.hoenheimsports.trainingservice.models.Hall;
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
public class HallRepositoryTest {

    @Autowired
    private HallRepository hallRepository;

    private Hall hall;

    @BeforeEach
    public void setup() {
        // Arrange
        Address address = Address.builder()
                .street("123 Main St")
                .city("Strasbourg")
                .postalCode("67000")
                .country("France")
                .build();

        hall = Hall.builder()
                .name("Main Hall")
                .address(address)
                .build();
    }

    @Test
    public void testSaveHall() {
        // Act
        Hall savedHall = hallRepository.save(hall);

        // Assert
        assertThat(savedHall).isNotNull();
        assertThat(savedHall.getId()).isNotNull();
        assertThat(savedHall.getName()).isEqualTo(hall.getName());
        assertThat(savedHall.getAddress()).isEqualTo(hall.getAddress());
    }

    @Test
    public void testFindById() {
        // Arrange
        Hall savedHall = hallRepository.save(hall);

        // Act
        Optional<Hall> foundHall = hallRepository.findById(savedHall.getId());

        // Assert
        assertThat(foundHall).isNotEmpty();
        assertThat(foundHall.get()).isEqualTo(savedHall);
    }

    @Test
    public void testFindAll() {
        // Arrange
        hallRepository.save(hall);

        // Act
        List<Hall> halls = hallRepository.findAll();

        // Assert
        assertThat(halls).isNotEmpty();
        assertThat(halls).contains(hall);
    }

    @Test
    public void testDeleteById() {
        // Arrange
        Hall savedHall = hallRepository.save(hall);

        // Act
        hallRepository.deleteById(savedHall.getId());
        Optional<Hall> foundHall = hallRepository.findById(savedHall.getId());

        // Assert
        assertThat(foundHall).isEmpty();
    }
}
