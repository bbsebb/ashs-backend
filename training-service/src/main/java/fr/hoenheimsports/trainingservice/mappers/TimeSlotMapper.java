package fr.hoenheimsports.trainingservice.mappers;

import fr.hoenheimsports.trainingservice.dto.TimeSlotDTO;
import fr.hoenheimsports.trainingservice.dto.request.TimeSlotDTORequest;

import fr.hoenheimsports.trainingservice.models.TimeSlot;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimeSlotMapper {

    TimeSlot toEntity(TimeSlotDTORequest timeSlotDtoRequest);

    TimeSlotDTO toDto(TimeSlot timeSlot);
}