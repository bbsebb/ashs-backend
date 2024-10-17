package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.TrainingSessionControllerImpl;
import fr.hoenheimsports.trainingservice.dto.AddressDto;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.dto.TimeSlotDto;
import fr.hoenheimsports.trainingservice.dto.TrainingSessionDto;

import fr.hoenheimsports.trainingservice.services.SortUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class TrainingSessionPagedModelAssemblerImplTest {

    @InjectMocks
    private TrainingSessionPagedModelAssemblerImpl trainingSessionPagedModelAssemblerImpl;

    @Mock
    private SortUtil sortUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToModelFirstPage() {
        // Arrange
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        TrainingSessionDto trainingSessionDto1 = new TrainingSessionDto(1L, timeSlotDto, hallDto);
        TrainingSessionDto trainingSessionDto2 = new TrainingSessionDto(2L, timeSlotDto, hallDto);

        EntityModel<TrainingSessionDto> trainingSessionModel1 = EntityModel.of(trainingSessionDto1);
        EntityModel<TrainingSessionDto> trainingSessionModel2 = EntityModel.of(trainingSessionDto2);

        List<EntityModel<TrainingSessionDto>> trainingSessionModels = List.of(trainingSessionModel1, trainingSessionModel2);
        Page<EntityModel<TrainingSessionDto>> page = new PageImpl<>(trainingSessionModels, PageRequest.of(0, 2, Sort.by("name")), 4);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<EntityModel<TrainingSessionDto>> pagedModel = (PagedModel<EntityModel<TrainingSessionDto>>) trainingSessionPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(trainingSessionModel1, trainingSessionModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(4);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(0);

        String baseUrl = linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(0, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(4);

        // Utilisation sécurisée de Optional avec ifPresent()
        pagedModel.getLink("self").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        pagedModel.getLink("next").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=0", "page=1"))
        );

        assertThat(pagedModel.getLink("prev")).isNotPresent();

        pagedModel.getLink("first").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        pagedModel.getLink("last").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=0", "page=1"))
        );
    }

    @Test
    public void testToModelLastPage() {
        // Arrange
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        TrainingSessionDto trainingSessionDto1 = new TrainingSessionDto(3L, timeSlotDto, hallDto);
        TrainingSessionDto trainingSessionDto2 = new TrainingSessionDto(4L, timeSlotDto, hallDto);

        EntityModel<TrainingSessionDto> trainingSessionModel1 = EntityModel.of(trainingSessionDto1);
        EntityModel<TrainingSessionDto> trainingSessionModel2 = EntityModel.of(trainingSessionDto2);

        List<EntityModel<TrainingSessionDto>> trainingSessionModels = List.of(trainingSessionModel1, trainingSessionModel2);
        Page<EntityModel<TrainingSessionDto>> page = new PageImpl<>(trainingSessionModels, PageRequest.of(1, 2, Sort.by("name")), 4);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<EntityModel<TrainingSessionDto>> pagedModel = (PagedModel<EntityModel<TrainingSessionDto>>) trainingSessionPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(trainingSessionModel1, trainingSessionModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(4);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(1);

        String baseUrl = linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(1, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(4);

        // Utilisation sécurisée de Optional avec ifPresent()
        pagedModel.getLink("self").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        assertThat(pagedModel.getLink("next")).isNotPresent();

        pagedModel.getLink("prev").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=0"))
        );

        pagedModel.getLink("first").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=0"))
        );

        pagedModel.getLink("last").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );
    }

    @Test
    public void testToModelSinglePage() {
        // Arrange
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        TrainingSessionDto trainingSessionDto1 = new TrainingSessionDto(1L, timeSlotDto, hallDto);
        TrainingSessionDto trainingSessionDto2 = new TrainingSessionDto(2L, timeSlotDto, hallDto);

        EntityModel<TrainingSessionDto> trainingSessionModel1 = EntityModel.of(trainingSessionDto1);
        EntityModel<TrainingSessionDto> trainingSessionModel2 = EntityModel.of(trainingSessionDto2);

        List<EntityModel<TrainingSessionDto>> trainingSessionModels = List.of(trainingSessionModel1, trainingSessionModel2);
        Page<EntityModel<TrainingSessionDto>> page = new PageImpl<>(trainingSessionModels, PageRequest.of(0, 2, Sort.by("name")), 2);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<EntityModel<TrainingSessionDto>> pagedModel = (PagedModel<EntityModel<TrainingSessionDto>>) trainingSessionPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(trainingSessionModel1, trainingSessionModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(1);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(0);

        String baseUrl = linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(0, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(3);

        // Utilisation sécurisée de Optional avec ifPresent()
        pagedModel.getLink("self").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        assertThat(pagedModel.getLink("next")).isNotPresent();
        assertThat(pagedModel.getLink("prev")).isNotPresent();

        pagedModel.getLink("first").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        pagedModel.getLink("last").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );
    }

    @Test
    public void testToModelMiddlePage() {
        // Arrange
        TimeSlotDto timeSlotDto = new TimeSlotDto(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        HallDto hallDto = new HallDto(1L, "Main Hall", new AddressDto("123 Street", "City", "12345", "Country"));
        TrainingSessionDto trainingSessionDto1 = new TrainingSessionDto(1L, timeSlotDto, hallDto);
        TrainingSessionDto trainingSessionDto2 = new TrainingSessionDto(2L, timeSlotDto, hallDto);

        EntityModel<TrainingSessionDto> trainingSessionModel1 = EntityModel.of(trainingSessionDto1);
        EntityModel<TrainingSessionDto> trainingSessionModel2 = EntityModel.of(trainingSessionDto2);

        List<EntityModel<TrainingSessionDto>> trainingSessionModels = List.of(trainingSessionModel1, trainingSessionModel2);
        Page<EntityModel<TrainingSessionDto>> page = new PageImpl<>(trainingSessionModels, PageRequest.of(1, 2, Sort.by("name")), 6);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<EntityModel<TrainingSessionDto>> pagedModel = (PagedModel<EntityModel<TrainingSessionDto>>) trainingSessionPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(trainingSessionModel1, trainingSessionModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(6);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(3);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(1);

        String baseUrl = linkTo(methodOn(TrainingSessionControllerImpl.class).getAllTrainingSessions(1, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(5);

        // Utilisation sécurisée de Optional avec ifPresent()
        pagedModel.getLink("self").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl)
        );

        pagedModel.getLink("next").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=2"))
        );

        pagedModel.getLink("prev").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=0"))
        );

        pagedModel.getLink("first").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=0"))
        );

        pagedModel.getLink("last").ifPresent(link ->
                assertThat(link.getHref()).isEqualTo(baseUrl.replace("page=1", "page=2"))
        );
    }
}
