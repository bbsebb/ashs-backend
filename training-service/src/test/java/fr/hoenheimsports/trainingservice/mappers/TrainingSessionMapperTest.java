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

@ExtendWith(MockitoExtension.class)
public class TrainingSessionMapperTest {

    @Mock
    private TimeSlotMapper timeSlotMapper;

    @Mock
    private HallMapper hallMapper;

    @InjectMocks
    private TrainingSessionMapperImpl trainingSessionMapper;

    private TimeSlot timeSlot;
    private Hall hall;
    private TrainingSession trainingSession;

    @BeforeEach
    public void setup() {
        timeSlot = new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        hall = Hall.builder()
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
    void toEntity_ValidDto_ReturnsEntity() {
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, timeSlotDto, hallDto);

        given(timeSlotMapper.toEntity(timeSlotDto)).willReturn(timeSlot);
        given(hallMapper.toEntity(hallDto)).willReturn(hall);

        TrainingSession result = trainingSessionMapper.toEntity(trainingSessionDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(trainingSessionDto.id());
        assertThat(result.getTimeSlot()).isEqualTo(timeSlot);
        assertThat(result.getHall()).isEqualTo(hall);
    }

    @Test
    void toEntity_NullDto_ReturnsNull() {
        TrainingSession result = trainingSessionMapper.toEntity(null);
        assertThat(result).isNull();
    }

    @Test
    void toDto_ValidEntity_ReturnsDto() {
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));

        given(timeSlotMapper.toDto(timeSlot)).willReturn(timeSlotDto);
        given(hallMapper.toDto(hall)).willReturn(hallDto);

        TrainingSessionDto result = trainingSessionMapper.toDto(trainingSession);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(trainingSession.getId());
        assertThat(result.timeSlot()).isEqualTo(timeSlotDto);
        assertThat(result.hall()).isEqualTo(hallDto);
    }

    @Test
    void toDto_NullEntity_ReturnsNull() {
        TrainingSessionDto result = trainingSessionMapper.toDto(null);
        assertThat(result).isNull();
    }

    @Test
    void partialUpdate_ValidDto_UpdatesEntity() {
        TimeSlotDto updateTimeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 0));
        TimeSlot updateTimeSlot = new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 0));
        HallDto updateHallDto = new HallDto(null, "Updated Hall", null);
        TrainingSessionDto updateDto = new TrainingSessionDto(null, updateTimeSlotDto, updateHallDto);



        given(timeSlotMapper.toEntity(updateTimeSlotDto)).willReturn(updateTimeSlot);
        willAnswer(invocation -> {
            HallDto dto = invocation.getArgument(0);
            Hall entity = invocation.getArgument(1);

            if (dto.id() != null) {
                entity.setId(dto.id());
            }
            if (dto.name() != null) {
                entity.setName(dto.name());
            }
            if (dto.address() != null) {
                if (entity.getAddress() == null) {
                    entity.setAddress(Address.builder().build());
                }
                AddressDto address = dto.address();
                Address entityAddress = entity.getAddress();
                if (address.street() != null) {
                    entityAddress.setStreet(address.street());
                }
                if (address.city() != null) {
                    entityAddress.setCity(address.city());
                }
                if (address.postalCode() != null) {
                    entityAddress.setPostalCode(address.postalCode());
                }
                if (address.country() != null) {
                    entityAddress.setCountry(address.country());
                }
            }

            return entity;
        }).given(hallMapper).partialUpdate(updateHallDto, hall);

        trainingSessionMapper.partialUpdate(updateDto, trainingSession);

        assertThat(trainingSession).isNotNull();
        assertThat(trainingSession.getTimeSlot().startTime()).isEqualTo(updateTimeSlotDto.startTime());
        assertThat(trainingSession.getHall().getName()).isEqualTo(updateHallDto.name());
    }

    @Test
    void partialUpdate_NullDto_DoesNotUpdateEntity() {
        trainingSessionMapper.partialUpdate(null, trainingSession);

        assertThat(trainingSession).isNotNull();
        assertThat(trainingSession.getTimeSlot().startTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(trainingSession.getHall().getName()).isEqualTo("Main Hall");
    }
}