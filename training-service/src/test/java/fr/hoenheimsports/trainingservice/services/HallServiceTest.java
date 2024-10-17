package fr.hoenheimsports.trainingservice.services;

import fr.hoenheimsports.trainingservice.Exception.HallNotFoundException;
import fr.hoenheimsports.trainingservice.assemblers.HallModelAssembler;
import fr.hoenheimsports.trainingservice.assemblers.HallPagedModelAssembler;
import fr.hoenheimsports.trainingservice.dto.AddressDto;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.mappers.HallMapper;
import fr.hoenheimsports.trainingservice.models.Address;
import fr.hoenheimsports.trainingservice.models.Hall;
import fr.hoenheimsports.trainingservice.repositories.HallRepository;
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
public class HallServiceTest {

    @Mock
    private HallRepository hallRepository;

    @Mock
    private HallModelAssembler hallModelAssembler;

    @Mock
    private HallPagedModelAssembler hallPagedModelAssembler;

    @Mock
    private HallMapper hallMapper;

    @Mock
    private SortUtil sortUtil;

    @InjectMocks
    private HallServiceImpl hallService;

    @Test
    public void testCreateHall() {
        // Arrange
        HallDto hallDto = new HallDto(null, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        Hall hall = Hall.builder()
                .name("Main Hall")
                .address(new Address("123 Street", "City", "12345", "Country"))
                .build();
        Hall savedHall = Hall.builder()
                .id(1L)
                .name("Main Hall")
                .address(new Address("123 Street", "City", "12345", "Country"))
                .build();
        EntityModel<HallDto> hallModel = EntityModel.of(new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country")));

        given(hallMapper.toDto(savedHall)).willReturn(new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country")));
        given(hallMapper.toEntity(hallDto)).willReturn(hall);
        given(hallRepository.save(any(Hall.class))).willReturn(savedHall);
        given(hallModelAssembler.toModel(any(HallDto.class))).willReturn(hallModel);

        // Act
        EntityModel<HallDto> createdHall = hallService.createHall(hallDto);

        // Assert
        assertThat(createdHall).isNotNull();
        assertThat(Objects.requireNonNull(createdHall.getContent()).id()).isEqualTo(1L);
        then(hallRepository).should(times(1)).save(any(Hall.class));
    }

    @Test
    public void testGetAllHalls() {
        // Arrange
        List<String> sortParams = List.of("name,asc");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        List<Hall> halls = Arrays.asList(
                Hall.builder()
                        .id(1L)
                        .name("Main Hall")
                        .address(new Address("123 Street", "City", "12345", "Country"))
                        .build(),
                Hall.builder()
                        .id(2L)
                        .name("Secondary Hall")
                        .address(new Address("456 Avenue", "Town", "67890", "Country"))
                        .build()
        );
        Page<Hall> hallPage = new PageImpl<>(halls, pageable, halls.size());
        List<HallDto> hallDtos = Arrays.asList(
                new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country")),
                new HallDto(2L, "Secondary Hall", new AddressDto("456 Avenue", "Town", "67890", "Country"))
        );
        List<EntityModel<HallDto>> hallModels = Arrays.asList(
                EntityModel.of(hallDtos.get(0)),
                EntityModel.of(hallDtos.get(1))
        );
        PagedModel<EntityModel<HallDto>> pagedModel = PagedModel.of(hallModels, new PagedModel.PageMetadata(10, 0, hallModels.size()));

        given(sortUtil.createSort(sortParams)).willReturn(pageable.getSort());
        given(hallRepository.findAll(any(Pageable.class))).willReturn(hallPage);
        given(hallMapper.toDto(any(Hall.class))).willReturn(hallDtos.get(0), hallDtos.get(1));
        given(hallPagedModelAssembler.toModel(ArgumentMatchers.any())).willAnswer(invocation -> {
            // Accès à l'argument Page<CoachModel> si nécessaire
            Page<EntityModel<HallDto>> page = invocation.getArgument(0);

            // Vous pouvez effectuer des opérations sur cet argument si nécessaire
            // et ensuite retourner l'objet simulé
            return pagedModel; // Retourne pagedModel
        });

        // Act
        PagedModel<EntityModel<HallDto>> result = (PagedModel<EntityModel<HallDto>>) hallService.getAllHalls(0, 10, sortParams);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2).containsExactlyInAnyOrderElementsOf(hallModels);
        then(hallRepository).should(times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testGetHallById_HallNotFoundException() {
        // Arrange
        given(hallRepository.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> hallService.getHallById(1L))
                .isInstanceOf(HallNotFoundException.class);

        then(hallRepository).should(times(1)).findById(1L);
    }

    @Test
    public void testGetHallById() throws HallNotFoundException {
        // Arrange
        Hall hall = Hall.builder()
                .id(1L)
                .name("Main Hall")
                .address(new Address("123 Street", "City", "12345", "Country"))
                .build();
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        EntityModel<HallDto> hallModel = EntityModel.of(hallDto);

        given(hallRepository.findById(1L)).willReturn(Optional.of(hall));
        given(hallMapper.toDto(hall)).willReturn(hallDto);
        given(hallModelAssembler.toModel(hallDto)).willReturn(hallModel);

        // Act
        EntityModel<HallDto> result = hallService.getHallById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(Objects.requireNonNull(result.getContent()).id()).isEqualTo(1L);
        then(hallRepository).should(times(1)).findById(1L);
    }

    @Test
    public void testUpdateHall_HallNotFoundException() {
        // Arrange
        HallDto hallDto = new HallDto(null, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        given(hallRepository.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> hallService.updateHall(1L, hallDto))
                .isInstanceOf(HallNotFoundException.class);

        then(hallRepository).should(times(1)).findById(1L);
        then(hallRepository).should(never()).save(any(Hall.class));
    }

    @Test
    public void testUpdateHall() throws HallNotFoundException {
        // Arrange
        HallDto hallDto = new HallDto(null, "Second Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        Hall hall = Hall.builder()
                .id(1L)
                .name("Main Hall")
                .address(new Address("123 Street", "City", "12345", "Country"))
                .build();
        Hall updatedHall = Hall.builder()
                .id(1L)
                .name("Second Hall")
                .address(new Address("123 Street", "City", "12345", "Country"))
                .build();
        HallDto updatedHallDto = new HallDto(1L, "Second Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        EntityModel<HallDto> updatedHallModel = EntityModel.of(updatedHallDto);

        given(hallRepository.findById(1L)).willReturn(Optional.of(hall));
        given(hallMapper.partialUpdate(hallDto, hall)).willReturn(updatedHall);
        given(hallMapper.toDto(updatedHall)).willReturn(updatedHallDto);
        given(hallRepository.save(any(Hall.class))).willReturn(updatedHall);
        given(hallModelAssembler.toModel(updatedHallDto)).willReturn(updatedHallModel);

        // Act
        EntityModel<HallDto> result = hallService.updateHall(1L, hallDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(Objects.requireNonNull(result.getContent()).id()).isEqualTo(1L);
        then(hallRepository).should(times(1)).findById(1L);
        then(hallRepository).should(times(1)).save(any(Hall.class));
    }

    @Test
    public void testDeleteHall() {
        // Arrange
        willDoNothing().given(hallRepository).deleteById(1L);

        // Act
        hallService.deleteHall(1L);

        // Assert
        then(hallRepository).should(times(1)).deleteById(1L);
    }
}