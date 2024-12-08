package fr.hoenheimsports.trainingservice.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class TimeSlotDTO extends RepresentationModel<TimeSlotDTO> {
    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;
}
