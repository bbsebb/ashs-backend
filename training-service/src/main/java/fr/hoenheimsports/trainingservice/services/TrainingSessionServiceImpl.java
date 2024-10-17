package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.TrainingSessionModelAssembler;
import fr.hoenheimsports.trainingservice.assemblers.TrainingSessionPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import fr.hoenheimsports.trainingservice.mappers.TrainingSessionMapper;
import fr.hoenheimsports.trainingservice.models.Hall;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import fr.hoenheimsports.trainingservice.repositories.HallRepository;
import fr.hoenheimsports.trainingservice.repositories.TrainingSessionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingSessionServiceImpl implements TrainingSessionService {
    private final TrainingSessionRepository trainingSessionRepository;
    private final HallRepository hallRepository;
    private final TrainingSessionModelAssembler trainingSessionModelAssembler;
    private final TrainingSessionPagedModelAssembler trainingSessionPagedModelAssembler;
    private final TrainingSessionMapper trainingSessionMapper;
    private final SortUtil sortUtil;

    public TrainingSessionServiceImpl(TrainingSessionRepository trainingSessionRepository, HallRepository hallRepository, TrainingSessionModelAssembler trainingSessionModelAssemblerImpl, TrainingSessionPagedModelAssembler trainingSessionPagedModelAssembler, TrainingSessionMapper trainingSessionMapper, SortUtil sortUtil) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.hallRepository = hallRepository;
        this.trainingSessionModelAssembler = trainingSessionModelAssemblerImpl;
        this.trainingSessionPagedModelAssembler = trainingSessionPagedModelAssembler;
        this.trainingSessionMapper = trainingSessionMapper;
        this.sortUtil = sortUtil;
    }

    @Override
    public EntityModel<TrainingSessionDto> createTrainingSession(TrainingSessionDto trainingSessionDto)  {
        TrainingSession newTrainingSession = this.trainingSessionMapper.toEntity(trainingSessionDto);
        persistRelatedEntities(newTrainingSession);
        newTrainingSession = this.trainingSessionRepository.save(newTrainingSession);
        return this.trainingSessionModelAssembler.toModel(this.trainingSessionMapper.toDto(newTrainingSession));
    }

    private void persistRelatedEntities(TrainingSession newTrainingSession) {
        if(newTrainingSession.getHall() != null) {
            Hall savedHall = this.hallRepository.save(newTrainingSession.getHall());
            newTrainingSession.setHall(savedHall);
        }
    }

    @Override
    public PagedModel<?> getAllTrainingSessions(int page, int size, List<String> sort) {
        Pageable pageable = PageRequest.of(page, size, this.sortUtil.createSort(sort));
        return trainingSessionPagedModelAssembler.toModel(trainingSessionRepository.findAll(pageable).map(trainingSessionMapper::toDto).map(trainingSessionModelAssembler::toModel));
    }

    @Override
    public EntityModel<TrainingSessionDto> getTrainingSessionById(Long id) throws TrainingSessionNotFoundException {
        return trainingSessionRepository.findById(id)
                .map(trainingSessionMapper::toDto)
                .map(trainingSessionModelAssembler::toModel)
                .orElseThrow(TrainingSessionNotFoundException::new);
    }

    @Override
    public EntityModel<TrainingSessionDto> updateTrainingSession(Long id, TrainingSessionDto trainingSessionDto) throws TrainingSessionNotFoundException {
        TrainingSession existingTrainingSession = trainingSessionRepository.findById(id)
                .orElseThrow(TrainingSessionNotFoundException::new);
        persistRelatedEntities(existingTrainingSession);
        TrainingSession updatedTrainingSession = trainingSessionMapper.partialUpdate(trainingSessionDto, existingTrainingSession);
        updatedTrainingSession = trainingSessionRepository.save(updatedTrainingSession);

        return trainingSessionModelAssembler.toModel(trainingSessionMapper.toDto(updatedTrainingSession));
    }

    @Override
    public void deleteTrainingSession(Long id) {
        trainingSessionRepository.deleteById(id);
    }
}