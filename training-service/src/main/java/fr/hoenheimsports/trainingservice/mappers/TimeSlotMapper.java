package fr.hoenheimsports.trainingservice.mappers;

import fr.hoenheimsports.trainingservice.dto.TimeSlotDto;
import fr.hoenheimsports.trainingservice.models.TimeSlot;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimeSlotMapper {

    TimeSlot toEntity(TimeSlotDto timeSlotDto);

    TimeSlotDto toDto(TimeSlot timeSlot);

}