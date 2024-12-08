package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.TrainingSessionAssembler;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDTO;
import fr.hoenheimsports.trainingservice.dto.request.TrainingSessionDTORequest;
import fr.hoenheimsports.trainingservice.mappers.TrainingSessionMapper;
import fr.hoenheimsports.trainingservice.models.Hall;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import fr.hoenheimsports.trainingservice.repositories.TrainingSessionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class TrainingSessionServiceImpl implements TrainingSessionService {
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionAssembler trainingSessionAssembler;
    private final TrainingSessionMapper trainingSessionMapper;
    private final HallServiceImpl hallServiceImpl;

    public TrainingSessionServiceImpl(TrainingSessionRepository trainingSessionRepository, TrainingSessionAssembler trainingSessionAssembler, TrainingSessionMapper trainingSessionMapper, HallServiceImpl hallServiceImpl) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.trainingSessionAssembler = trainingSessionAssembler;

        this.trainingSessionMapper = trainingSessionMapper;
        this.hallServiceImpl = hallServiceImpl;
    }

    @Override
    public TrainingSessionDTO createAndConvertToModel(TrainingSessionDTORequest trainingSessionDtoRequest) {
        return this.trainingSessionAssembler.toModel(this.create(trainingSessionDtoRequest));
    }

    private TrainingSession create(TrainingSessionDTORequest trainingSessionDtoRequest) {
        Hall hall = this.hallServiceImpl.findOrCreateOrUpdate(trainingSessionDtoRequest.hall());
        TrainingSession newTrainingSession = this.trainingSessionMapper.toEntity(trainingSessionDtoRequest);
        hall.addTrainingSession(newTrainingSession);
        newTrainingSession = this.trainingSessionRepository.save(newTrainingSession);
        return newTrainingSession;
    }

    @Override
    public TrainingSession findOrCreateOrUpdate(TrainingSessionDTORequest trainingSessionDtoRequest) {
        if(trainingSessionDtoRequest == null) {
            throw new TrainingSessionNotFoundException();
        }
        TrainingSession trainingSession;
        if(trainingSessionDtoRequest.id() != null) {
            trainingSession = this.update(trainingSessionDtoRequest.id(), trainingSessionDtoRequest);
        } else {
            trainingSession =  this.create(trainingSessionDtoRequest);
        }
        return trainingSession;
    }

    @Override
    public PagedModel<TrainingSessionDTO> getAllModels(Pageable pageable) {
        return trainingSessionAssembler.toPagedModel(trainingSessionRepository.findAll(pageable));
    }

    @Override
    public Page<TrainingSession> getAll(Pageable pageable) {
        return trainingSessionRepository.findAll(pageable);
    }


    @Override
    public TrainingSessionDTO getModelById(Long id){
        return trainingSessionRepository.findById(id)
                .map(trainingSessionAssembler::toModel)
                .orElseThrow(TrainingSessionNotFoundException::new);
    }

    @Override
    public TrainingSessionDTO updateAndConvertToModel(Long id, TrainingSessionDTORequest trainingSessionDtoRequest) {
        return trainingSessionAssembler.toModel(this.update(trainingSessionDtoRequest.id(), trainingSessionDtoRequest));
    }

    private TrainingSession update(Long id, TrainingSessionDTORequest trainingSessionDtoRequest)   {
        Hall hall = this.hallServiceImpl.findOrCreateOrUpdate(trainingSessionDtoRequest.hall());
        TrainingSession existingTrainingSession = trainingSessionRepository.findById(id)
                .orElseThrow(TrainingSessionNotFoundException::new);
        TrainingSession updatedTrainingSession = trainingSessionMapper.partialUpdate(trainingSessionDtoRequest, existingTrainingSession);
        hall.addTrainingSession(updatedTrainingSession);
        updatedTrainingSession = trainingSessionRepository.save(updatedTrainingSession);
        return updatedTrainingSession;
    }

    @Override
    public void deleteById(Long id) {
        trainingSessionRepository.deleteById(id);
    }
}