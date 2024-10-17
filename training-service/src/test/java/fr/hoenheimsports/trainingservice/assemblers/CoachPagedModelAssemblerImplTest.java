package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.CoachControllerImpl;
import fr.hoenheimsports.trainingservice.dto.CoachDto;
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

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CoachPagedModelAssemblerImplTest {

    @InjectMocks
    private CoachPagedModelAssemblerImpl coachPagedModelAssemblerImpl;

    @Mock
    private SortUtil sortUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToModelFirstPage() {
        // Arrange
        CoachDto coachDto1 = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        CoachDto coachDto2 = new CoachDto(2L, "Jane", "Doe", "jane.doe@example.com", "0987654321");

        EntityModel<CoachDto> coachModel1 = EntityModel.of(coachDto1);
        EntityModel<CoachDto>  coachModel2 = EntityModel.of(coachDto2);

        List<EntityModel<CoachDto> > coachModels = List.of(coachModel1, coachModel2);
        Page<EntityModel<CoachDto> > page = new PageImpl<>(coachModels, PageRequest.of(0, 2, Sort.by("name")), 4);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<EntityModel<CoachDto> > pagedModel = (PagedModel<EntityModel<CoachDto> >) coachPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(coachModel1, coachModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(4);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(0);

        String baseUrl = linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(0, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(4);
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
        CoachDto coachDto1 = new CoachDto(3L, "Jim", "Beam", "jim.beam@example.com", "1122334455");
        CoachDto coachDto2 = new CoachDto(4L, "Jack", "Daniels", "jack.daniels@example.com", "5566778899");

        EntityModel<CoachDto>  coachModel1 = EntityModel.of(coachDto1);
        EntityModel<CoachDto>  coachModel2 = EntityModel.of(coachDto2);

        List<EntityModel<CoachDto> > coachModels = List.of(coachModel1, coachModel2);
        Page<EntityModel<CoachDto> > page = new PageImpl<>(coachModels, PageRequest.of(1, 2, Sort.by("name")), 4);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<EntityModel<CoachDto> > pagedModel = (PagedModel<EntityModel<CoachDto> >) coachPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(coachModel1, coachModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(4);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(1);

        String baseUrl = linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(1, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(4);
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
        CoachDto coachDto1 = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        CoachDto coachDto2 = new CoachDto(2L, "Jane", "Doe", "jane.doe@example.com", "0987654321");

        EntityModel<CoachDto>  coachModel1 = EntityModel.of(coachDto1);
        EntityModel<CoachDto>  coachModel2 = EntityModel.of(coachDto2);

        List<EntityModel<CoachDto> > coachModels = List.of(coachModel1, coachModel2);
        Page<EntityModel<CoachDto> > page = new PageImpl<>(coachModels, PageRequest.of(0, 2, Sort.by("name")), 2);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<EntityModel<CoachDto> > pagedModel = (PagedModel<EntityModel<CoachDto> >) coachPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(coachModel1, coachModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(1);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(0);

        String baseUrl = linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(0, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(3);
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
        CoachDto coachDto1 = new CoachDto(1L, "John", "Doe", "john.doe@example.com", "1234567890");
        CoachDto coachDto2 = new CoachDto(2L, "Jane", "Doe", "jane.doe@example.com", "0987654321");

        EntityModel<CoachDto> coachModel1 =  EntityModel.of(coachDto1);
        EntityModel<CoachDto> coachModel2 =  EntityModel.of(coachDto2);

        List<EntityModel<CoachDto>> coachModels = List.of(coachModel1, coachModel2);
        Page<EntityModel<CoachDto>> page = new PageImpl<>(coachModels, PageRequest.of(1, 2, Sort.by("name")), 6);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<EntityModel<CoachDto>> pagedModel = (PagedModel<EntityModel<CoachDto>>)  coachPagedModelAssemblerImpl.toModel(page);


        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(coachModel1, coachModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(6);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(3);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(1);

        String baseUrl = linkTo(methodOn(CoachControllerImpl.class).getAllCoaches(1, 2, List.of("name,asc"))).toUri().toString();

        assertThat(pagedModel.getLinks()).hasSize(5);
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