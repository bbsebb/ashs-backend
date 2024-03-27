package fr.hoenheimsports.instagramservice.controllers;

import fr.hoenheimsports.instagramservice.models.Media;
import fr.hoenheimsports.instagramservice.services.GraphInstagramService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(GraphInstagramController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class GraphInstagramControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GraphInstagramService graphInstagramService;
    @Test
    public void getMedias_ShouldReturnListOfMedias() throws Exception {
        // Given
        Media media1 = Media.builder("1")
                .mediaType(fr.hoenheimsports.instagramservice.models.MediaType.IMAGE)
                .mediaUrl(URI.create("http://example.com/media1.jpg").toURL())
                .username("user1")
                .caption("Caption 1")
                .timestamp(OffsetDateTime.now())
                .permalink(URI.create("http://example.com/permalink1").toURL())
                .thumbnailUrl(URI.create("http://example.com/thumbnail1.jpg").toURL())
                .build();

        Media media2 = Media.builder("2")
                .mediaType(fr.hoenheimsports.instagramservice.models.MediaType.VIDEO)
                .mediaUrl(URI.create("http://example.com/media2.mp4").toURL())
                .username("user2")
                .caption("Caption 2")
                .timestamp(OffsetDateTime.now())
                .permalink(URI.create("http://example.com/permalink2").toURL())
                .thumbnailUrl(URI.create("http://example.com/thumbnail2.jpg").toURL())
                .build();

        List<Media> medias = Arrays.asList(media1, media2);

        given(graphInstagramService.getMedias()).willReturn(medias);
        // When & Then
        mockMvc.perform(get("/api/media")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{'id':'1'},{'id':'2'}]")); // Simplified response check for example purposes
    }
}