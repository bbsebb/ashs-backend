package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.feignClient.GraphInstagram;
import fr.hoenheimsports.instagramservice.feignClient.dto.MediaDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.MediaType;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserMediasDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserProfileDTO;
import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.models.Media;
import fr.hoenheimsports.instagramservice.repositories.AccessTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GraphInstagramServiceimplTest {

    @Mock
    private GraphInstagram graphInstagram;

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @InjectMocks
    private GraphInstagramServiceimpl graphInstagramServiceImpl;

    @Test
    void getMedias_shouldReturnListMedia() throws MalformedURLException {
        // Given
        when(graphInstagram.getAllMediaByUser(anyString(), anyString(), anyString())).thenReturn(
                new UserMediasDTO(
                        List.of(
                                MediaDTO.builder()
                                        .id("id1")
                                        .caption("caption")
                                        .mediaUrl(URI.create("http://localhost").toURL())
                                        .permalink(URI.create("http://localhost").toURL())
                                        .thumbnailUrl(URI.create("http://localhost").toURL())
                                        .timestamp(OffsetDateTime.now())
                                        .username("username")
                                        .mediaType(fr.hoenheimsports.instagramservice.feignClient.dto.MediaType.IMAGE)
                                        .build(),
                                MediaDTO.builder()
                                        .id("id2")
                                        .caption("caption2")
                                        .mediaUrl(URI.create("http://localhost2").toURL())
                                        .permalink(URI.create("http://localhost2").toURL())
                                        .thumbnailUrl(URI.create("http://localhost2").toURL())
                                        .timestamp(OffsetDateTime.now())
                                        .username("username2")
                                        .mediaType(MediaType.VIDEO)
                                        .build()
                        ),
                        null
                )
        );
        when(accessTokenRepository.get()).thenReturn(AccessToken.builder().accessToken("token").build());
        when(graphInstagram.getMe(anyString(), anyString())).thenReturn(UserProfileDTO.builder().id("id").build());

        // When
        List<Media> medias = this.graphInstagramServiceImpl.getMedias();
        // Then

        verify(graphInstagram, times(1)).getAllMediaByUser(anyString(), anyString(), anyString());
        verify(accessTokenRepository, times(2)).get();
        verify(graphInstagram, times(1)).getMe(anyString(), anyString());
        verify(graphInstagram, times(0)).getChildrenMediaByMediaId(anyString(), anyString(), anyString());
        assertEquals(2, medias.size());

    }

    @Test
    void getMedias_shouldReturnListMediaWithChildren() throws MalformedURLException {
        // Given
        MediaDTO mediaDTO1 = MediaDTO.builder()
                .id("id1")
                .caption("caption")
                .mediaUrl(URI.create("http://localhost").toURL())
                .permalink(URI.create("http://localhost").toURL())
                .thumbnailUrl(URI.create("http://localhost").toURL())
                .timestamp(OffsetDateTime.now())
                .username("username")
                .mediaType(fr.hoenheimsports.instagramservice.feignClient.dto.MediaType.IMAGE)
                .build();
        MediaDTO mediaDTO2 = MediaDTO.builder()
                .id("id2")
                .caption("caption2")
                .mediaUrl(URI.create("http://localhost2").toURL())
                .permalink(URI.create("http://localhost2").toURL())
                .thumbnailUrl(URI.create("http://localhost2").toURL())
                .timestamp(OffsetDateTime.now())
                .username("username2")
                .mediaType(MediaType.CAROUSEL_ALBUM)
                .build();
        MediaDTO mediaDTO3 = MediaDTO.builder()
                .id("id3")
                .caption(null)
                .mediaUrl(URI.create("http://localhost3").toURL())
                .permalink(URI.create("http://localhost3").toURL())
                .thumbnailUrl(URI.create("http://localhost3").toURL())
                .timestamp(OffsetDateTime.now())
                .username("username3")
                .mediaType(fr.hoenheimsports.instagramservice.feignClient.dto.MediaType.IMAGE)
                .build();
        MediaDTO mediaDTO4 = MediaDTO.builder()
                .id("id4")
                .caption(null)
                .mediaUrl(URI.create("http://localhost4").toURL())
                .permalink(URI.create("http://localhost4").toURL())
                .thumbnailUrl(URI.create("http://localhost4").toURL())
                .timestamp(OffsetDateTime.now())
                .username("username4")
                .mediaType(MediaType.VIDEO)
                .build();

        when(graphInstagram.getAllMediaByUser(anyString(), anyString(), anyString())).thenReturn(
                new UserMediasDTO(List.of(mediaDTO1, mediaDTO2), null)
        );
        when(accessTokenRepository.get()).thenReturn(AccessToken.builder().accessToken("token").build());
        when(graphInstagram.getMe(anyString(), anyString())).thenReturn(UserProfileDTO.builder().id("id").build());
        when(graphInstagram.getChildrenMediaByMediaId(anyString(), anyString(), anyString())).thenReturn(
                new UserMediasDTO(List.of(mediaDTO3, mediaDTO4), null)
        );
        // When

        List<Media> medias = this.graphInstagramServiceImpl.getMedias();
        // Then
        verify(graphInstagram, times(1)).getAllMediaByUser(anyString(), anyString(), anyString());
        verify(accessTokenRepository, times(3)).get();
        verify(graphInstagram, times(1)).getMe(anyString(), anyString());
        verify(graphInstagram, times(1)).getChildrenMediaByMediaId(anyString(), anyString(), anyString());
        assertEquals(2, medias.size());
        assertEquals(2, medias.get(1).getChildren().size());
        // Assertions for mediaDTO1
        assertEquals("id1", medias.get(0).getId());
        assertEquals(MediaType.IMAGE.toString(), medias.get(0).getMediaType().toString());
        assertEquals("http://localhost", medias.getFirst().getMediaUrl().toString());
        assertEquals("username", medias.getFirst().getUsername());
        assertEquals("caption", medias.getFirst().getCaption());
        assertEquals(mediaDTO1.timestamp(), medias.getFirst().getTimestamp());
        assertEquals("http://localhost", medias.getFirst().getPermalink().toString());
        assertEquals("http://localhost", medias.getFirst().getThumbnailUrl().toString());
        assertNull(medias.get(0).getChildren());

// Assertions for mediaDTO2
        assertEquals("id2", medias.get(1).getId());
        assertEquals(MediaType.CAROUSEL_ALBUM.toString(), medias.get(1).getMediaType().toString());
        assertEquals("http://localhost2", medias.get(1).getMediaUrl().toString());
        assertEquals("username2", medias.get(1).getUsername());
        assertEquals("caption2", medias.get(1).getCaption());
        assertEquals(mediaDTO2.timestamp(), medias.get(1).getTimestamp());
        assertEquals("http://localhost2", medias.get(1).getPermalink().toString());
        assertEquals("http://localhost2", medias.get(1).getThumbnailUrl().toString());
        assertEquals(2, medias.get(1).getChildren().size());

// Assertions for mediaDTO3 (first child of mediaDTO2)
        assertEquals("id3", medias.get(1).getChildren().getFirst().getId());
        assertEquals(MediaType.IMAGE.toString(), medias.get(1).getChildren().getFirst().getMediaType().toString());
        assertEquals("http://localhost3", medias.get(1).getChildren().getFirst().getMediaUrl().toString());
        assertEquals("username3", medias.get(1).getChildren().getFirst().getUsername());
        assertNull(medias.get(1).getChildren().getFirst().getCaption());
        assertEquals(mediaDTO3.timestamp(), medias.get(1).getChildren().getFirst().getTimestamp());
        assertEquals("http://localhost3", medias.get(1).getChildren().getFirst().getPermalink().toString());
        assertEquals("http://localhost3", medias.get(1).getChildren().getFirst().getThumbnailUrl().toString());
        assertNull(medias.get(1).getChildren().get(0).getChildren());

// Assertions for mediaDTO4 (second child of mediaDTO2)
        assertEquals("id4", medias.get(1).getChildren().get(1).getId());
        assertEquals(MediaType.VIDEO.toString(), medias.get(1).getChildren().get(1).getMediaType().toString());
        assertEquals("http://localhost4", medias.get(1).getChildren().get(1).getMediaUrl().toString());
        assertEquals("username4", medias.get(1).getChildren().get(1).getUsername());
        assertNull(medias.get(1).getChildren().get(1).getCaption());
        assertEquals(mediaDTO4.timestamp(), medias.get(1).getChildren().get(1).getTimestamp());
        assertEquals("http://localhost4", medias.get(1).getChildren().get(1).getPermalink().toString());
        assertEquals("http://localhost4", medias.get(1).getChildren().get(1).getThumbnailUrl().toString());
        assertNull(medias.get(1).getChildren().get(1).getChildren());
    }
}