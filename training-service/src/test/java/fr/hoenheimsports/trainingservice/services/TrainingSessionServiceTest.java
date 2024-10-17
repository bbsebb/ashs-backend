package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.DataNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.Exception.TrainingSessionNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.TrainingSessionModelAssembler;
import fr.hoenheimsports.trainingservice.assemblers.TrainingSessionPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.TimeSlotDto;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;
import fr.hoenheimsports.trainingservice.mappers.TrainingSessionMapper;
import fr.hoenheimsports.trainingservice.models.TimeSlot;
import fr.hoenheimsports.trainingservice.models.TrainingSession;
import fr.hoenheimsports.trainingservice.repositories.HallRepository;
import fr.hoenheimsports.trainingservice.repositories.TrainingSessionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingSessionServiceTest {

    @Mock
    private TrainingSessionRepository trainingSessionRepository;

    @Mock
    private HallRepository hallRepository;

    @Mock
    private TrainingSessionModelAssembler trainingSessionModelAssembler;

    @Mock
    private TrainingSessionPagedModelAssembler trainingSessionPagedModelAssembler;

    @Mock
    private TrainingSessionMapper trainingSessionMapper;

    @Mock
    private SortUtil sortUtil;

    @InjectMocks
    private TrainingSessionServiceImpl trainingSessionService;

    @Test
    void createTrainingSession_ValidTrainingSessionDto_ReturnsTrainingSessionModel() throws HallNotFoundException {
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(null, new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)), null);
        TrainingSession trainingSession = TrainingSession.builder()
                .timeSlot(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)))
                .build();
        TrainingSession savedTrainingSession = TrainingSession.builder()
                .id(1L)
                .timeSlot(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)))
                .build();
        EntityModel<TrainingSessionDto> trainingSessionModel = EntityModel.of(new TrainingSessionDto(1L, new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)), null));

        given(trainingSessionMapper.toEntity(trainingSessionDto)).willReturn(trainingSession);
        given(trainingSessionRepository.save(any(TrainingSession.class))).willReturn(savedTrainingSession);
//        given(hallRepository.save(any())).willReturn(savedTrainingSession.getHall());
        given(trainingSessionMapper.toDto(savedTrainingSession)).willReturn(new TrainingSessionDto(1L, new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)), null));
        given(trainingSessionModelAssembler.toModel(any(TrainingSessionDto.class))).willReturn(trainingSessionModel);

        EntityModel<TrainingSessionDto> createdTrainingSession = trainingSessionService.createTrainingSession(trainingSessionDto);

        assertThat(createdTrainingSession).isNotNull();
        assertThat(Objects.requireNonNull(createdTrainingSession.getContent()).id()).isEqualTo(1L);
        then(trainingSessionRepository).should(times(1)).save(any(TrainingSession.class));
    }

    @Test
    void getAllTrainingSessions_ValidPageAndSize_ReturnsPagedModel() {
        List<String> sortParams = List.of("timeSlot.dayOfWeek,asc");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("timeSlot.dayOfWeek").ascending());
        List<TrainingSession> trainingSessions = Arrays.asList(
                TrainingSession.builder()
                        .id(1L)
                        .timeSlot(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)))
                        .build(),
                TrainingSession.builder()
                        .id(2L)
                        .timeSlot(new TimeSlot(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)))
                        .build()
        );
        Page<TrainingSession> trainingSessionPage = new PageImpl<>(trainingSessions, pageable, trainingSessions.size());
        List<TrainingSessionDto> trainingSessionDtos = Arrays.asList(
                new TrainingSessionDto(1L, new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)), null),
                new TrainingSessionDto(2L, new TimeSlotDto(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)), null)
        );
        List<EntityModel<TrainingSessionDto>> trainingSessionModels = Arrays.asList(
                EntityModel.of(trainingSessionDtos.get(0)),
                EntityModel.of(trainingSessionDtos.get(1))
        );
        PagedModel<EntityModel<TrainingSessionDto>> pagedModel = PagedModel.of(trainingSessionModels, new PagedModel.PageMetadata(10, 0, trainingSessionModels.size()));

        given(sortUtil.createSort(sortParams)).willReturn(pageable.getSort());
        given(trainingSessionRepository.findAll(any(Pageable.class))).willReturn(trainingSessionPage);

        given(trainingSessionMapper.toDto(any(TrainingSession.class))).willReturn(trainingSessionDtos.get(0), trainingSessionDtos.get(1));

        given(trainingSessionPagedModelAssembler.toModel(ArgumentMatchers.any())).willAnswer(invocation -> {
            // Accès à l'argument Page<CoachModel> si nécessaire
            Page<EntityModel<TrainingSessionDto>> page = invocation.getArgument(0);

            // Vous pouvez effectuer des opérations sur cet argument si nécessaire
            // et ensuite retourner l'objet simulé
            return pagedModel; // Retourne pagedModel
        });

        PagedModel<EntityModel<TrainingSessionDto>> result = (PagedModel<EntityModel<TrainingSessionDto>>) trainingSessionService.getAllTrainingSessions(0, 10, sortParams);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2).containsExactlyInAnyOrderElementsOf(trainingSessionModels);
        then(trainingSessionRepository).should(times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getTrainingSessionById_TrainingSessionNotFoundException() {
        given(trainingSessionRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> trainingSessionService.getTrainingSessionById(1L))
                .isInstanceOf(TrainingSessionNotFoundException.class);

        then(trainingSessionRepository).should(times(1)).findById(1L);
    }

    @Test
    void getTrainingSessionById_ValidId_ReturnsTrainingSessionModel() throws TrainingSessionNotFoundException {
        TrainingSession trainingSession = TrainingSession.builder()
                .id(1L)
                .timeSlot(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)))
                .build();
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(1L, new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)), null);
        EntityModel<TrainingSessionDto> trainingSessionModel = EntityModel.of(trainingSessionDto);

        given(trainingSessionRepository.findById(1L)).willReturn(Optional.of(trainingSession));
        given(trainingSessionMapper.toDto(trainingSession)).willReturn(trainingSessionDto);
        given(trainingSessionModelAssembler.toModel(trainingSessionDto)).willReturn(trainingSessionModel);

        EntityModel<TrainingSessionDto> result = trainingSessionService.getTrainingSessionById(1L);

        assertThat(result).isNotNull();
        assertThat(Objects.requireNonNull(result.getContent()).id()).isEqualTo(1L);
        then(trainingSessionRepository).should(times(1)).findById(1L);
    }

    @Test
    void updateTrainingSession_TrainingSessionNotFoundException() {
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(null, new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)), null);
        given(trainingSessionRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> trainingSessionService.updateTrainingSession(1L, trainingSessionDto))
                .isInstanceOf(TrainingSessionNotFoundException.class);

        then(trainingSessionRepository).should(times(1)).findById(1L);
        then(trainingSessionRepository).should(never()).save(any(TrainingSession.class));
    }

    @Test
    void updateTrainingSession_ValidIdAndTrainingSessionDto_ReturnsUpdatedTrainingSessionModel() throws DataNotFoundException {
        TrainingSessionDto trainingSessionDto = new TrainingSessionDto(null, new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)), null);
        TrainingSession trainingSession = TrainingSession.builder()
                .id(1L)
                .timeSlot(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)))
                .build();
        TrainingSession updatedTrainingSession = TrainingSession.builder()
                .id(1L)
                .timeSlot(new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)))
                .build();
        TrainingSessionDto updatedTrainingSessionDto = new TrainingSessionDto(1L, new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0)), null);
        EntityModel<TrainingSessionDto> updatedTrainingSessionModel = EntityModel.of(updatedTrainingSessionDto);

        given(trainingSessionRepository.findById(1L)).willReturn(Optional.of(trainingSession));
//        given(hallRepository.save(any())).willReturn(trainingSession.getHall());
        given(trainingSessionMapper.partialUpdate(trainingSessionDto, trainingSession)).willReturn(updatedTrainingSession);
        given(trainingSessionRepository.save(any(TrainingSession.class))).willReturn(updatedTrainingSession);
        given(trainingSessionMapper.toDto(updatedTrainingSession)).willReturn(updatedTrainingSessionDto);
        given(trainingSessionModelAssembler.toModel(updatedTrainingSessionDto)).willReturn(updatedTrainingSessionModel);

        EntityModel<TrainingSessionDto> result = trainingSessionService.updateTrainingSession(1L, trainingSessionDto);

        assertThat(result).isNotNull();
        assertThat(Objects.requireNonNull(result.getContent()).id()).isEqualTo(1L);
        then(trainingSessionRepository).should(times(1)).findById(1L);
        then(trainingSessionRepository).should(times(1)).save(any(TrainingSession.class));
    }

    @Test
    void deleteTrainingSession_ValidId_DeletesTrainingSession() {
        willDoNothing().given(trainingSessionRepository).deleteById(1L);

        trainingSessionService.deleteTrainingSession(1L);

        then(trainingSessionRepository).should(times(1)).deleteById(1L);
    }
}