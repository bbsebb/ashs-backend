package fr.hoenheimsports.instagramservice.feignClient;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import fr.hoenheimsports.instagramservice.feignClient.dto.LongLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserMediasDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserProfileDTO;
import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.repositories.AccessTokenFirestoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWireMock(port = 8090)
class GraphInstagramTest {
    @Autowired
    private GraphInstagram graphInstagram;
    @MockBean
    private AccessTokenFirestoreRepository accessTokenFirestoreRepository;
    @MockBean
    private Firestore firestore;
    @MockBean
    private FirebaseApp firebaseApp;
    @MockBean
    private GoogleCredentials googleCredentials;



    @BeforeEach
    void setUp() {
        given(accessTokenFirestoreRepository.get()).willReturn(AccessToken.builder().accessToken("accessToken").expireIn(100000000).tokenType("Baerer").build());
    }

    @Test
    void getMe_shouldReturnUserProfileDTO() {
        //given
        UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                .accountType("personal")
                .id("01")
                .mediaCount(1)
                .username("username")
                .build();
        String userProfileDTOJson = """
        {
            "account_type": "%s",
            "id": "%s",
            "media_count": %d,
            "username": "%s"
        }
        """.formatted(userProfileDTO.accountType(), userProfileDTO.id(), userProfileDTO.mediaCount(), userProfileDTO.username());
        stubFor(get(urlPathEqualTo("/me"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(userProfileDTOJson)));
        //when
        UserProfileDTO userProfileDTOReponse = graphInstagram.getMe("id,username,account_type,media_count", "ACCESS_TOKEN");
        //then
        assertEquals(userProfileDTO.accountType(), userProfileDTOReponse.accountType());
    }

    @Test
    void getAllMediaByUser_shouldReturnUserMediasDTO() {
        //given
        String UserMediasDTOJson = "{\"data\":[{\"id\":\"01\",\"caption\":\"caption\",\"media_type\":\"IMAGE\",\"media_url\":\"http://localhost/media_url\",\"permalink\":\"http://localhost/permalink\",\"thumbnail_url\":\"http://localhost/thumbnail_url\",\"timestamp\":\"100\",\"username\":\"username\"}]}";
        stubFor(get(urlPathEqualTo("/01/media"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(UserMediasDTOJson)));
        //when
        UserMediasDTO userMediasDTO = graphInstagram.getAllMediaByUser("01", "id,caption,media_type,media_url,permalink,thumbnail_url,timestamp,username", "ACCESS_TOKEN");
        //then
        assertEquals("01", userMediasDTO.data().getFirst().id());
    }

    @Test
    void getChildrenMediaByMediaId_shouldReturnUserMediasDTO() {
        //given
        String UserMediasDTOJson = "{\"data\":[{\"id\":\"01\",\"caption\":\"caption\",\"media_type\":\"IMAGE\",\"media_url\":\"http://localhost/media_url\",\"permalink\":\"http://localhost/permalink\",\"thumbnail_url\":\"http://localhost/thumbnail_url\",\"timestamp\":\"100\",\"username\":\"username\"}]}";
        stubFor(get(urlPathEqualTo("/01/children"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(UserMediasDTOJson)));
        //when
        UserMediasDTO userMediasDTO = graphInstagram.getChildrenMediaByMediaId("01", "id,caption,media_type,media_url,permalink,thumbnail_url,timestamp,username", "ACCESS_TOKEN");
        //then
        assertEquals("01", userMediasDTO.data().getFirst().id());
    }

    @Test
    void getLongLivedAccessToken_shouldReturnLongLivedAccessTokenDTO() {
        //given
        String LongLivedAccessTokenDTOJson = "{\"access_token\":\"ACCESS_TOKEN\",\"token_type\":\"Bearer\",\"expires_in\":5184000}";
        stubFor(get(urlPathEqualTo("/access_token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(LongLivedAccessTokenDTOJson)));
        //when
        LongLivedAccessTokenDTO longLivedAccessTokenDTO = graphInstagram.getLongLivedAccessToken("authorization_code", "client_secret", "ACCESS_TOKEN");
        //then
        assertEquals("ACCESS_TOKEN", longLivedAccessTokenDTO.accessToken());
    }

    @Test
    void refreshAccessToken_shouldReturnLongLivedAccessTokenDTO() {
        //given
        String LongLivedAccessTokenDTOJson = "{\"access_token\":\"ACCESS_TOKEN\",\"token_type\":\"Bearer\",\"expires_in\":5184000}";
        stubFor(get(urlPathEqualTo("/refresh_access_token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(LongLivedAccessTokenDTOJson)));
        //when
        LongLivedAccessTokenDTO longLivedAccessTokenDTO = graphInstagram.refreshAccessToken("authorization_code", "ACCESS_TOKEN");
        //then
        assertEquals("ACCESS_TOKEN", longLivedAccessTokenDTO.accessToken());
    }
}