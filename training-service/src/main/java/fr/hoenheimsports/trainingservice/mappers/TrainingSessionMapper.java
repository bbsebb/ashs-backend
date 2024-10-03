package fr.hoenheimsports.trainingservice.mappers;

import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {TimeSlotMapper.class,HallMapper.class})
public interface TrainingSessionMapper {


    TrainingSession toEntity(TrainingSessionDto trainingSessionDto);

    TrainingSessionDto toDto(TrainingSession trainingSession);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TrainingSession partialUpdate(TrainingSessionDto trainingSessionDto,@MappingTarget TrainingSession existingTrainingSession);
}