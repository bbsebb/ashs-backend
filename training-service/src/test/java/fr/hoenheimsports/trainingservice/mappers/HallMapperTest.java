package fr.hoenheimsports.trainingservice.mappers;

import fr.hoenheimsports.trainingservice.dto.AddressDto;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.models.Address;
import fr.hoenheimsports.trainingservice.models.Hall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class HallMapperTest {

    private HallMapper hallMapper;

    @BeforeEach
    public void setup() {
        hallMapper = Mappers.getMapper(HallMapper.class);
    }

    @Test
    public void testToEntity() {
        // Arrange
        AddressDto addressDto = new AddressDto("123 Street", "City", "12345", "Country");
        HallDto hallDto = new HallDto(1L, "Main Hall", addressDto);

        // Act
        Hall hall = hallMapper.toEntity(hallDto);

        // Assert
        assertThat(hall).isNotNull();
        assertThat(hall.getId()).isEqualTo(hallDto.id());
        assertThat(hall.getName()).isEqualTo(hallDto.name());
        assertThat(hall.getAddress().getStreet()).isEqualTo(hallDto.address().street());
        assertThat(hall.getAddress().getCity()).isEqualTo(hallDto.address().city());
        assertThat(hall.getAddress().getPostalCode()).isEqualTo(hallDto.address().postalCode());
        assertThat(hall.getAddress().getCountry()).isEqualTo(hallDto.address().country());
    }

    @Test
    public void testToDto() {
        // Arrange
        Address address = new Address("123 Street", "City", "12345", "Country");
        Hall hall = new Hall();
        hall.setId(1L);
        hall.setName("Main Hall");
        hall.setAddress(address);

        // Act
        HallDto hallDto = hallMapper.toDto(hall);

        // Assert
        assertThat(hallDto).isNotNull();
        assertThat(hallDto.id()).isEqualTo(hall.getId());
        assertThat(hallDto.name()).isEqualTo(hall.getName());
        assertThat(hallDto.address().street()).isEqualTo(hall.getAddress().getStreet());
        assertThat(hallDto.address().city()).isEqualTo(hall.getAddress().getCity());
        assertThat(hallDto.address().postalCode()).isEqualTo(hall.getAddress().getPostalCode());
        assertThat(hallDto.address().country()).isEqualTo(hall.getAddress().getCountry());
    }

    @Test
    public void testPartialUpdate() {
        // Arrange
        AddressDto addressDto = new AddressDto("123 Street", null, "12345", null);
        HallDto hallDto = new HallDto(null, "Main Hall", addressDto);
        Address address = new Address("456 Avenue", "Old City", "67890", "Old Country");
        Hall hall = new Hall();
        hall.setId(1L);
        hall.setName("Old Hall");
        hall.setAddress(address);

        // Act
        hallMapper.partialUpdate(hallDto, hall);

        // Assert
        assertThat(hall).isNotNull();
        assertThat(hall.getId()).isEqualTo(1L); // id should not be updated
        assertThat(hall.getName()).isEqualTo(hallDto.name()); // name should be updated
        assertThat(hall.getAddress().getStreet()).isEqualTo(addressDto.street()); // street should be updated
        assertThat(hall.getAddress().getCity()).isEqualTo("Old City"); // city should not be updated
        assertThat(hall.getAddress().getPostalCode()).isEqualTo(addressDto.postalCode()); // postal code should be updated
        assertThat(hall.getAddress().getCountry()).isEqualTo("Old Country"); // country should not be updated
    }
}