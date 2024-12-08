package fr.hoenheimsports.trainingservice.mappers;

import fr.hoenheimsports.trainingservice.dto.TeamDTO;
import fr.hoenheimsports.trainingservice.dto.request.TeamDTORequest;
import fr.hoenheimsports.trainingservice.models.Team;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {TimeSlotMapper.class, CoachMapper.class,TrainingSessionMapper.class})
public interface TeamMapper {
    Team toEntity(TeamDTORequest teamDtoRequest);

    TeamDTO toDto(Team team);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "trainingSessions", ignore = true)
    @Mapping(target = "coaches", ignore = true)
    Team partialUpdate(TeamDTORequest teamDtoRequest, @MappingTarget Team team);
}