package fr.hoenheimsports.trainingservice.mappers;

import fr.hoenheimsports.trainingservice.dto.HallDTO;
import fr.hoenheimsports.trainingservice.dto.request.HallDTORequest;
import fr.hoenheimsports.trainingservice.models.Hall;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HallMapper {
    Hall toEntity(HallDTORequest hallDtoRequest);

    HallDTO toDto(Hall hall);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Hall partialUpdate(HallDTORequest hallDtoRequest, @MappingTarget Hall hall);
}