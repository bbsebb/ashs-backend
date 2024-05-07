package fr.hoenheimsports.instagramservice.controllers;

import fr.hoenheimsports.instagramservice.models.Media;
import fr.hoenheimsports.instagramservice.services.GraphInstagramService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GraphInstagramControllerTest {

    @Mock
    private GraphInstagramService graphInstagramService;

    @InjectMocks
    private GraphInstagramController graphInstagramController;



    @Test
    public void getMediasReturnsExpectedMediaList() {
        Media media = Media.builder("id").build();
        List<Media> expectedMediaList = Collections.singletonList(media);
        given(graphInstagramService.getMedias()).willReturn(expectedMediaList);

        ResponseEntity<List<Media>> result = graphInstagramController.getMedias();

        assertThat(result.getBody()).isEqualTo(expectedMediaList);
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    public void getMediasReturnsEmptyListWhenNoMedia() {
        given(graphInstagramService.getMedias()).willReturn(Collections.emptyList());

        ResponseEntity<List<Media>> result = graphInstagramController.getMedias();

        assertThat(result.getBody()).isEmpty();
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }
}