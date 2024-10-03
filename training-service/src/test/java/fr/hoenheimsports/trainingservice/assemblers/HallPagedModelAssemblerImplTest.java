package fr.hoenheimsports.trainingservice.assemblers;

import fr.hoenheimsports.trainingservice.controllers.HallControllerImpl;
import fr.hoenheimsports.trainingservice.dto.AddressDto;
import fr.hoenheimsports.trainingservice.dto.HallDto;
import fr.hoenheimsports.trainingservice.ressources.HallModel;
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
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class HallPagedModelAssemblerImplTest {

    @InjectMocks
    private HallPagedModelAssemblerImpl hallPagedModelAssemblerImpl;

    @Mock
    private SortUtil sortUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToModelFirstPage() {
        // Arrange
        AddressDto addressDto = new AddressDto("123 Street", "City", "12345", "Country");
        HallDto hallDto1 = new HallDto(1L, "Main Hall", addressDto);
        HallDto hallDto2 = new HallDto(2L, "Secondary Hall", addressDto);

        HallModel hallModel1 = new HallModel(hallDto1);
        HallModel hallModel2 = new HallModel(hallDto2);

        List<HallModel> hallModels = List.of(hallModel1, hallModel2);
        Page<HallModel> page = new PageImpl<>(hallModels, PageRequest.of(0, 2, Sort.by("name")), 4);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<HallModel> pagedModel = hallPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(hallModel1, hallModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(4);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(0);

        String baseUrl = linkTo(methodOn(HallControllerImpl.class).getAllHalls(0, 2, List.of("name,asc"))).toUri().toString();

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
        AddressDto addressDto = new AddressDto("123 Street", "City", "12345", "Country");
        HallDto hallDto1 = new HallDto(3L, "Main Hall", addressDto);
        HallDto hallDto2 = new HallDto(4L, "Secondary Hall", addressDto);

        HallModel hallModel1 = new HallModel(hallDto1);
        HallModel hallModel2 = new HallModel(hallDto2);

        List<HallModel> hallModels = List.of(hallModel1, hallModel2);
        Page<HallModel> page = new PageImpl<>(hallModels, PageRequest.of(1, 2, Sort.by("name")), 4);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<HallModel> pagedModel = hallPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(hallModel1, hallModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(4);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(1);

        String baseUrl = linkTo(methodOn(HallControllerImpl.class).getAllHalls(1, 2, List.of("name,asc"))).toUri().toString();

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
        AddressDto addressDto = new AddressDto("123 Street", "City", "12345", "Country");
        HallDto hallDto1 = new HallDto(1L, "Main Hall", addressDto);
        HallDto hallDto2 = new HallDto(2L, "Secondary Hall", addressDto);

        HallModel hallModel1 = new HallModel(hallDto1);
        HallModel hallModel2 = new HallModel(hallDto2);

        List<HallModel> hallModels = List.of(hallModel1, hallModel2);
        Page<HallModel> page = new PageImpl<>(hallModels, PageRequest.of(0, 2, Sort.by("name")), 2);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<HallModel> pagedModel = hallPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(hallModel1, hallModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(1);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(0);

        String baseUrl = linkTo(methodOn(HallControllerImpl.class).getAllHalls(0, 2, List.of("name,asc"))).toUri().toString();

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
        AddressDto addressDto = new AddressDto("123 Street", "City", "12345", "Country");
        HallDto hallDto1 = new HallDto(1L, "Main Hall", addressDto);
        HallDto hallDto2 = new HallDto(2L, "Secondary Hall", addressDto);

        HallModel hallModel1 = new HallModel(hallDto1);
        HallModel hallModel2 = new HallModel(hallDto2);

        List<HallModel> hallModels = List.of(hallModel1, hallModel2);
        Page<HallModel> page = new PageImpl<>(hallModels, PageRequest.of(1, 2, Sort.by("name")), 6);

        given(sortUtil.createSortParams(Sort.by("name"))).willReturn(List.of("name,asc"));

        // Act
        PagedModel<HallModel> pagedModel = hallPagedModelAssemblerImpl.toModel(page);

        // Assert
        assertThat(pagedModel).isNotNull();
        assertThat(pagedModel.getContent()).hasSize(2).contains(hallModel1, hallModel2);
        assertThat(Objects.requireNonNull(pagedModel.getMetadata()).getSize()).isEqualTo(2);
        assertThat(pagedModel.getMetadata().getTotalElements()).isEqualTo(6);
        assertThat(pagedModel.getMetadata().getTotalPages()).isEqualTo(3);
        assertThat(pagedModel.getMetadata().getNumber()).isEqualTo(1);

        String baseUrl = linkTo(methodOn(HallControllerImpl.class).getAllHalls(1, 2, List.of("name,asc"))).toUri().toString();

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