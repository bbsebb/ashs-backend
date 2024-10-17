package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.CoachNotFoundException;

import fr.hoenheimsports.trainingservice.assemblers.CoachModelAssemblerImpl;
import fr.hoenheimsports.trainingservice.assemblers.CoachPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
import fr.hoenheimsports.trainingservice.mappers.CoachMapper;
import fr.hoenheimsports.trainingservice.models.Coach;
import fr.hoenheimsports.trainingservice.repositories.CoachRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class CoachServiceTest {

    @Mock
    private CoachRepository coachRepository;

    @Mock
    private CoachModelAssemblerImpl coachModelAssemblerImpl;

    @Mock
    private CoachPagedModelAssembler coachPagedModelAssemblerImpl;

    @Mock
    private CoachMapper coachMapper;

    @Mock
    private SortUtil sortUtil;

    @InjectMocks
    private CoachServiceImpl coachService;


    @Test
    public void testCreateCoach() {
        // Arrange
        CoachDto coachDto = new CoachDto(null, "John", "Doe", "john.doe@example.com", "1234567890");

        Coach savedCoach = new Coach();
        savedCoach.setId(1L);
        savedCoach.setName("John");
        savedCoach.setSurname("Doe");
        savedCoach.setEmail("john.doe@example.com");
        savedCoach.setPhone("1234567890");
        EntityModel<CoachDto> coachModel = EntityModel.of(new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890"));

        given(coachRepository.save(any(Coach.class))).willReturn(savedCoach);
        given(coachMapper.toDto(savedCoach)).willReturn(new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890"));
        given(coachModelAssemblerImpl.toModel(any(CoachDto.class))).willReturn(coachModel);

        // Act
        EntityModel<CoachDto> createdCoach = coachService.createCoach(coachDto);

        // Assert
        assertThat(createdCoach).isNotNull();
        assertThat(Objects.requireNonNull(createdCoach.getContent()).id()).isEqualTo(1L);
        then(coachRepository).should(times(1)).save(any(Coach.class));
    }

    @Test
    public void testGetAllCoaches() {
        // Arrange
        List<String> sortParams = List.of("name,asc");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        List<Coach> coaches = Arrays.asList(
                Coach.builder()
                        .id(1L)
                        .name("John")
                        .surname("Doe")
                        .email("john.doe@example.com")
                        .phone("1234567890")
                        .build(),
                Coach.builder()
                        .id(2L)
                        .name("Jane")
                        .surname("Doe")
                        .email("jane.doe@example.com")
                        .phone("0987654321")
                        .build()
        );
        Page<Coach> coachPage = new PageImpl<>(coaches, pageable, coaches.size());
        List<CoachDto> coachDtos = Arrays.asList(
                new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890"),
                new CoachDto(2L, "Jane", "Doe", "jane.doe@example.com", "0987654321")
        );
        List<EntityModel<CoachDto>> coachModels = Arrays.asList(
                EntityModel.of(coachDtos.get(0)),
                EntityModel.of(coachDtos.get(1))
        );
        PagedModel<?> pagedModel =  PagedModel.of(coachModels, new PagedModel.PageMetadata(10, 0, coachModels.size()));

        given(sortUtil.createSort(sortParams)).willReturn(pageable.getSort());
        given(coachRepository.findAll(any(Pageable.class))).willReturn(coachPage);
        given(coachMapper.toDto(any(Coach.class))).willReturn(coachDtos.get(0), coachDtos.get(1));
        given(coachPagedModelAssemblerImpl.toModel(ArgumentMatchers.any())).willAnswer(invocation -> {
            // Accès à l'argument Page<EntityModel<CoachDto>> si nécessaire
            Page<EntityModel<CoachDto>> page = invocation.getArgument(0);

            // Vous pouvez effectuer des opérations sur cet argument si nécessaire
            // et ensuite retourner l'objet simulé
            return pagedModel; // Retourne pagedModel
        });


        // Act
        PagedModel<EntityModel<CoachDto>> result = (PagedModel<EntityModel<CoachDto>>) coachService.getAllCoaches(0, 10, sortParams);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2).containsExactlyInAnyOrderElementsOf(coachModels);
        then(coachRepository).should(times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testGetCoachById_CoachNotFoundException() {
        // Arrange
        given(coachRepository.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> coachService.getCoachById(1L))
                .isInstanceOf(CoachNotFoundException.class);

        then(coachRepository).should(times(1)).findById(1L);
    }


    @Test
    public void testGetCoachById() throws CoachNotFoundException {
        // Arrange
        Coach coach = Coach.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
        CoachDto coachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        EntityModel<CoachDto> coachModel = EntityModel.of(coachDto);

        given(coachRepository.findById(1L)).willReturn(Optional.of(coach));
        given(coachMapper.toDto(coach)).willReturn(coachDto);
        given(coachModelAssemblerImpl.toModel(coachDto)).willReturn(coachModel);

        // Act
        EntityModel<CoachDto> result = coachService.getCoachById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(Objects.requireNonNull(result.getContent()).id()).isEqualTo(1L);
        then(coachRepository).should(times(1)).findById(1L);
    }

    @Test
    public void testUpdateCoach_CoachNotFoundException() {
        // Arrange
        CoachDto coachDto = new CoachDto(null, "John", "Doe", "john.doe@example.com", "1234567890");
        given(coachRepository.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> coachService.updateCoach(1L, coachDto))
                .isInstanceOf(CoachNotFoundException.class);

        then(coachRepository).should(times(1)).findById(1L);
        then(coachRepository).should(never()).save(any(Coach.class));
    }

    @Test
    public void testUpdateCoach() throws CoachNotFoundException {
        // Arrange
        CoachDto coachDto = new CoachDto(null, "John", "Doe", "john.doe@example.com", "1234567890");
        Coach coach = Coach.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
        Coach updatedCoach = Coach.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
        CoachDto updatedCoachDto = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        EntityModel<CoachDto> updatedEntityModel = EntityModel.of(updatedCoachDto);

        given(coachRepository.findById(1L)).willReturn(Optional.of(coach));
        given(coachRepository.save(any(Coach.class))).willReturn(updatedCoach);
        given(coachMapper.toDto(updatedCoach)).willReturn(updatedCoachDto);
        given(coachModelAssemblerImpl.toModel(updatedCoachDto)).willReturn(updatedEntityModel);

        // Act
        EntityModel<CoachDto> result = coachService.updateCoach(1L, coachDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(Objects.requireNonNull(result.getContent()).id()).isEqualTo(1L);
        then(coachRepository).should(times(1)).findById(1L);
        then(coachRepository).should(times(1)).save(any(Coach.class));
    }

    @Test
    public void testDeleteCoach() {
        // Arrange
        willDoNothing().given(coachRepository).deleteById(1L);

        // Act
        coachService.deleteCoach(1L);

        // Assert
        then(coachRepository).should(times(1)).deleteById(1L);
    }



}
