package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.TrainingSessionModelAssembler;
import fr.hoenheimsports.trainingservice.assemblers.TrainingSessionPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import fr.hoenheimsports.trainingservice.mappers.TrainingSessionMapper;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import fr.hoenheimsports.trainingservice.repositories.TrainingSessionRepository;
import fr.hoenheimsports.trainingservice.ressources.TrainingSessionModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingSessionServiceImpl implements TrainingSessionService {
    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionModelAssembler trainingSessionModelAssembler;
    private final TrainingSessionPagedModelAssembler trainingSessionPagedModelAssembler;
    private final TrainingSessionMapper trainingSessionMapper;
    private final SortUtil sortUtil;

    public TrainingSessionServiceImpl(TrainingSessionRepository trainingSessionRepository, TrainingSessionModelAssembler trainingSessionModelAssemblerImpl,TrainingSessionPagedModelAssembler trainingSessionPagedModelAssembler, TrainingSessionMapper trainingSessionMapper, SortUtil sortUtil) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.trainingSessionModelAssembler = trainingSessionModelAssemblerImpl;
        this.trainingSessionPagedModelAssembler = trainingSessionPagedModelAssembler;
        this.trainingSessionMapper = trainingSessionMapper;
        this.sortUtil = sortUtil;
    }

    @Override
    public TrainingSessionModel createTrainingSession(TrainingSessionDto trainingSessionDto) {
       TrainingSession newTrainingSession = this.trainingSessionRepository.save(this.trainingSessionMapper.toEntity(trainingSessionDto));
         return this.trainingSessionModelAssembler.toModel(this.trainingSessionMapper.toDto(newTrainingSession));
    }

    @Override
    public PagedModel<TrainingSessionModel> getAllTrainingSessions(int page, int size, List<String> sort) {
        Pageable pageable = PageRequest.of(page, size, this.sortUtil.createSort(sort));
        return trainingSessionPagedModelAssembler.toModel(trainingSessionRepository.findAll(pageable).map(trainingSessionMapper::toDto).map(trainingSessionModelAssembler::toModel));
    }

    @Override
    public TrainingSessionModel getTrainingSessionById(Long id) throws TrainingSessionNotFoundException {
        return trainingSessionRepository.findById(id)
                .map(trainingSessionMapper::toDto)
                .map(trainingSessionModelAssembler::toModel)
                .orElseThrow(TrainingSessionNotFoundException::new);
    }

    @Override
    public TrainingSessionModel updateTrainingSession(Long id, TrainingSessionDto trainingSessionDto) throws TrainingSessionNotFoundException {
        TrainingSession existingTrainingSession = trainingSessionRepository.findById(id)
                .orElseThrow(TrainingSessionNotFoundException::new);

        TrainingSession updatedTrainingSession = trainingSessionMapper.partialUpdate(trainingSessionDto, existingTrainingSession);
        updatedTrainingSession = trainingSessionRepository.save(updatedTrainingSession);

        return trainingSessionModelAssembler.toModel(trainingSessionMapper.toDto(updatedTrainingSession));
    }

    @Override
    public void deleteTrainingSession(Long id) {
        trainingSessionRepository.deleteById(id);
    }
}