package fr.hoenheimsports.trainingservice.mappers;

import fr.hoenheimsports.trainingservice.dto.TrainingSessionDTO;
import fr.hoenheimsports.trainingservice.dto.request.TrainingSessionDTORequest;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {TimeSlotMapper.class,HallMapper.class})
public interface TrainingSessionMapper {


    TrainingSession toEntity(TrainingSessionDTORequest trainingSessionDtoRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TrainingSession partialUpdate(TrainingSessionDTORequest trainingSessionDtoRequest, @MappingTarget TrainingSession existingTrainingSession);

    TrainingSessionDTO toDto(TrainingSession trainingSession);
}