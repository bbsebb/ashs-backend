package fr.hoenheimsports.trainingservice.mappers;

import fr.hoenheimsports.trainingservice.dto.CoachDto;
import fr.hoenheimsports.trainingservice.models.Coach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;


public class CoachMapperTest {

    private CoachMapper coachMapper;

    @BeforeEach
    public void setup() {
        coachMapper = Mappers.getMapper(CoachMapper.class);
    }

    @Test
    public void testToEntity() {
        // Arrange
        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");

        // Act
        Coach coach = coachMapper.toEntity(coachDto);

        // Assert
        assertThat(coach).isNotNull();
        assertThat(coach.getId()).isEqualTo(coachDto.id());
        assertThat(coach.getName()).isEqualTo(coachDto.name());
        assertThat(coach.getSurname()).isEqualTo(coachDto.surname());
        assertThat(coach.getEmail()).isEqualTo(coachDto.email());
        assertThat(coach.getPhone()).isEqualTo(coachDto.phone());
    }

    @Test
    public void testToDto() {
        // Arrange
        Coach coach = new Coach();
        coach.setId(1L);
        coach.setName("John");
        coach.setSurname("Doe");
        coach.setEmail("john.doe@example.com");
        coach.setPhone("1234567890");

        // Act
        CoachDto coachDto = coachMapper.toDto(coach);

        // Assert
        assertThat(coachDto).isNotNull();
        assertThat(coachDto.id()).isEqualTo(coach.getId());
        assertThat(coachDto.name()).isEqualTo(coach.getName());
        assertThat(coachDto.surname()).isEqualTo(coach.getSurname());
        assertThat(coachDto.email()).isEqualTo(coach.getEmail());
        assertThat(coachDto.phone()).isEqualTo(coach.getPhone());
    }

    @Test
    public void testPartialUpdate() {
        // Arrange
        CoachDto coachDto = new CoachDto(null, "John", null, "john.doe@example.com", null);
        Coach coach = new Coach();
        coach.setId(1L);
        coach.setName("Jane");
        coach.setSurname("Smith");
        coach.setEmail("jane.smith@example.com");
        coach.setPhone("0987654321");

        // Act
        coachMapper.partialUpdate(coachDto, coach);

        // Assert
        assertThat(coach).isNotNull();
        assertThat(coach.getId()).isEqualTo(1L); // id should not be updated
        assertThat(coach.getName()).isEqualTo(coachDto.name()); // name should be updated
        assertThat(coach.getSurname()).isEqualTo("Smith"); // surname should not be updated
        assertThat(coach.getEmail()).isEqualTo(coachDto.email()); // email should be updated
        assertThat(coach.getPhone()).isEqualTo("0987654321"); // phone should not be updated
    }
}