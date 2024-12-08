package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.CoachAssembler;
import fr.hoenheimsports.trainingservice.dto.CoachDTO;
import fr.hoenheimsports.trainingservice.dto.request.CoachDTORequest;
import fr.hoenheimsports.trainingservice.mappers.CoachMapper;
import fr.hoenheimsports.trainingservice.models.Coach;
import fr.hoenheimsports.trainingservice.repositories.CoachRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class CoachServiceImpl implements CoachService {
    private final CoachRepository coachRepository;
    private final CoachAssembler coachAssembler;
    private final CoachMapper coachMapper;

    public CoachServiceImpl(CoachRepository coachRepository, CoachAssembler coachAssembler, CoachMapper coachMapper) {
        this.coachRepository = coachRepository;
        this.coachAssembler = coachAssembler;
        this.coachMapper = coachMapper;
    }

    @Override
    public CoachDTO createAndConvertToModel(CoachDTORequest coachDtoRequest) {
        return coachAssembler.toModel(this.create(coachDtoRequest));
    }

    private Coach create(CoachDTORequest coachDtoRequest) {
        Coach newCoach = coachMapper.toEntity(coachDtoRequest);
        return coachRepository.save(newCoach);
    }

    @Override
    public Coach findOrCreateOrUpdate(CoachDTORequest coachDtoRequest) throws CoachNotFoundException {
        if(coachDtoRequest == null) {
            throw new CoachNotFoundException();
        }
        Coach coach;
        if(coachDtoRequest.id() != null) {
            coach =  this.update(coachDtoRequest.id(), coachDtoRequest);
        } else {
            coach =  this.create(coachDtoRequest);
        }
        return this.coachRepository.save(coach);
    }

    @Override
    public PagedModel<CoachDTO> getAllModels(Pageable pageable) {
        return coachAssembler.toPagedModel(coachRepository.findAll(pageable));
    }

    @Override
    public CoachDTO getModelById(Long id) throws CoachNotFoundException {
        return coachRepository.findById(id).map(coachAssembler::toModel).orElseThrow(CoachNotFoundException::new);
    }

    @Override
    public CoachDTO updateAndConvertToModel(Long id, CoachDTORequest coachDtoRequest) throws CoachNotFoundException {
        return coachAssembler.toModel(this.update(id, coachDtoRequest));
    }

    private Coach update(Long id, CoachDTORequest coachDtoRequest) throws CoachNotFoundException {
        Coach existingCoach = coachRepository.findById(id)
                .orElseThrow(CoachNotFoundException::new);
        Coach updatedCoach = coachMapper.partialUpdate(coachDtoRequest, existingCoach);
        updatedCoach = coachRepository.save(updatedCoach);
        return updatedCoach;
    }

    @Override
    public void deleteById(Long id) {
        coachRepository.deleteById(id);
    }
}