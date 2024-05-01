package fr.hoenheimsports.instagramservice.services;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.hoenheimsports.instagramservice.feignClient.GraphInstagram;
import fr.hoenheimsports.instagramservice.feignClient.dto.MediaDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.MediaType;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserMediasDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserProfileDTO;
import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.models.Media;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.time.OffsetDateTime;
import java.net.URL;

@ExtendWith(MockitoExtension.class)
class GraphInstagramServiceImplTest {

    @Mock
    private GraphInstagram graphInstagram;

    @Mock
    private AuthInstagramService authInstagramService;

    @InjectMocks
    private GraphInstagramServiceImpl service;


    @Test
    void getMediasReturnsListOfMediaCorrectly() throws Exception {
        // Given values to be tested
        String expectedCaption1 = "Caption1";
        String expectedCaption2 = "Caption2";
        String id1 = "1";
        String id2 = "2";
        boolean isSharedToFeed1 = false;
        boolean isSharedToFeed2 = true;
        MediaType expectedMediaType1 = MediaType.IMAGE;
        MediaType expectedMediaType2 = MediaType.VIDEO;
        URL expectedMediaUrl1 = URI.create("http://example.com/image1").toURL();
        URL expectedMediaUrl2 = URI.create("http://example.com/video1").toURL();
        URL permalink1 = URI.create("http://example.com/image1").toURL();
        URL permalink2 = URI.create("http://example.com/video1").toURL();
        URL thumbnail1 = URI.create("http://example.com/image1").toURL();
        URL thumbnail2 = URI.create("http://example.com/video1").toURL();
        OffsetDateTime expectedTimestamp1 = OffsetDateTime.now();
        OffsetDateTime expectedTimestamp2 = OffsetDateTime.now().minusDays(1);
        String username1 = "user1";
        String username2 = "user2";


        UserMediasDTO userMediasDTO = new UserMediasDTO(Arrays.asList(
                new MediaDTO(expectedCaption1,id1,isSharedToFeed1,expectedMediaType1,expectedMediaUrl1,permalink1,thumbnail1,expectedTimestamp1,username1),
                new MediaDTO(expectedCaption2,id2,isSharedToFeed2,expectedMediaType2,expectedMediaUrl2,permalink2,thumbnail2,expectedTimestamp2,username2)
        ), null);
        AccessToken accessToken = AccessToken.builder()
                .accessToken("validAccessToken")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1))
                .tokenType("Bearer")
                .build();
        String userId = "1";
        UserProfileDTO userProfileDTO = new UserProfileDTO("personal", userId, 2, "user1");

        given(graphInstagram.getAllMediaByUser(anyString(), anyString(), anyString())).willReturn(userMediasDTO);
        given(authInstagramService.retrieveAccessToken()).willReturn(accessToken);
        given(graphInstagram.getMe(anyString(), anyString())).willReturn(userProfileDTO);

        // When
        List<Media> medias = service.getMedias();

        // Then
        assertThat(medias).hasSize(2);

        Media media1 = medias.get(0);
        Media media2 = medias.get(1);

        assertThat(media1.getCaption()).isEqualTo(expectedCaption1);
        assertThat(media1.getMediaType().toString()).isEqualTo(expectedMediaType1.toString());
        assertThat(media1.getMediaUrl()).isEqualTo(expectedMediaUrl1);
        assertThat(media1.getTimestamp()).isEqualToIgnoringSeconds(expectedTimestamp1);
        assertThat(media1.getUsername()).isEqualTo(username1);
        assertThat(media1.getPermalink()).isEqualTo(permalink1);
        assertThat(media1.getThumbnailUrl()).isEqualTo(thumbnail1);


        assertThat(media2.getCaption()).isEqualTo(expectedCaption2);
        assertThat(media2.getMediaType().toString()).isEqualTo(expectedMediaType2.toString());
        assertThat(media2.getMediaUrl()).isEqualTo(expectedMediaUrl2);
        assertThat(media2.getTimestamp()).isEqualToIgnoringSeconds(expectedTimestamp2);
        assertThat(media2.getUsername()).isEqualTo(username2);
        assertThat(media2.getPermalink()).isEqualTo(permalink2);
        assertThat(media2.getThumbnailUrl()).isEqualTo(thumbnail2);

    }


    @Test
    void getMediasHandlesMediaAndSubMediaCorrectly() throws Exception {
        // Prepare data for main and sub-media
        URL mainMediaUrl = URI.create("https://example.com/carousel1").toURL();
        URL subMediaUrl = URI.create("https://example.com/image2").toURL();
        OffsetDateTime mainTimestamp = OffsetDateTime.now();
        OffsetDateTime subTimestamp = OffsetDateTime.now().minusHours(2);

        UserMediasDTO subMediasDTO = new UserMediasDTO(List.of(
                new MediaDTO("SubCaption", "sub1", false, MediaType.IMAGE, subMediaUrl, subMediaUrl, subMediaUrl, subTimestamp, "userSub")
        ), null);
        UserMediasDTO userMediasDTO = new UserMediasDTO(List.of(
                new MediaDTO("Caption", "1", false, MediaType.CAROUSEL_ALBUM, mainMediaUrl, mainMediaUrl, mainMediaUrl, mainTimestamp, "userMain")
        ), null);

        // Mock the service methods
        given(graphInstagram.getAllMediaByUser(anyString(), anyString(), anyString())).willReturn(userMediasDTO);
        given(graphInstagram.getChildrenMediaByMediaId(eq("1"), anyString(), anyString())).willReturn(subMediasDTO);
        AccessToken accessToken = AccessToken.builder()
                .accessToken("validAccessToken")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1))
                .tokenType("Bearer")
                .build();
        given(authInstagramService.retrieveAccessToken()).willReturn(accessToken);
        UserProfileDTO userProfileDTO =  new UserProfileDTO("personal", "1", 2, "userMain");
        given(graphInstagram.getMe(anyString(), anyString())).willReturn(userProfileDTO);

        // Execute the method under test
        List<Media> medias = service.getMedias();

        // Assert conditions
        assertThat(medias).hasSize(1);
        assertThat(medias.getFirst().getCaption()).isEqualTo("Caption");
        assertThat(medias.getFirst().getMediaType().toString()).isEqualTo(MediaType.CAROUSEL_ALBUM.toString());
        assertThat(medias.getFirst().getChildren()).hasSize(1);
        assertThat(medias.getFirst().getChildren().getFirst().getCaption()).isEqualTo("SubCaption");

        // Verify that the correct APIs are called with expected parameters
        then(graphInstagram).should().getAllMediaByUser(anyString(), anyString(), eq("validAccessToken"));
        then(graphInstagram).should().getChildrenMediaByMediaId(eq("1"), anyString(), eq("validAccessToken"));
    }
}
