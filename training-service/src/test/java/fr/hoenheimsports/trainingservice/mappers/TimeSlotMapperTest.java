package fr.hoenheimsports.trainingservice.mappers;

import fr.hoenheimsports.trainingservice.dto.TimeSlotDto;
import fr.hoenheimsports.trainingservice.models.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TimeSlotMapperTest {

    @InjectMocks
    private TimeSlotMapperImpl timeSlotMapper;

    private TimeSlot timeSlot;
    private TimeSlotDto timeSlotDto;
    @BeforeEach
    void setUp() {
        timeSlot = new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
    }

    @Test
    void toEntity_ValidDto_ReturnsEntity() {
        TimeSlot result = timeSlotMapper.toEntity(timeSlotDto);
        assertThat(result).isNotNull();
        assertThat(result.dayOfWeek()).isEqualTo(timeSlotDto.dayOfWeek());
        assertThat(result.startTime()).isEqualTo(timeSlotDto.startTime());
        assertThat(result.endTime()).isEqualTo(timeSlotDto.endTime());
    }
    @Test
    void toEntity_NullDto_ReturnsNull() {
        TimeSlot result = timeSlotMapper.toEntity(null);
        assertThat(result).isNull();
    }
    @Test
    void toDto_ValidEntity_ReturnsDto() {
        TimeSlotDto result = timeSlotMapper.toDto(timeSlot);
        assertThat(result).isNotNull();
        assertThat(result.dayOfWeek()).isEqualTo(timeSlot.dayOfWeek());
        assertThat(result.startTime()).isEqualTo(timeSlot.startTime());
        assertThat(result.endTime()).isEqualTo(timeSlot.endTime());
    }
    @Test
    void toDto_NullEntity_ReturnsNull() {
        TimeSlotDto result = timeSlotMapper.toDto(null);
        assertThat(result).isNull();
    }


}