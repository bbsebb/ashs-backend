package fr.hoenheimsports.instagramservice.feignClient;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import fr.hoenheimsports.instagramservice.config.InstragramAPIProperties;
import fr.hoenheimsports.instagramservice.exceptions.IGAPIOAuthException;
import fr.hoenheimsports.instagramservice.exceptions.InstagramAPIException;
import fr.hoenheimsports.instagramservice.feignClient.dto.ShortLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.repositories.AccessTokenFirestoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@AutoConfigureWireMock(port = 8090)
@SpringBootTest
@ActiveProfiles("test")
class ApiInstagramTest {

    @Autowired
    private ApiInstagram apiInstagram;
    @MockBean
    private InstragramAPIProperties instragramAPIProperties;
    @MockBean
    private AccessTokenFirestoreRepository accessTokenFirestoreRepository;
    @MockBean
    private Firestore firestore;
    @MockBean
    private FirebaseApp firebaseApp;
    @MockBean
    private GoogleCredentials googleCredentials;
    private Map<String, String> formData;

    @BeforeEach
    void setUp() {
        formData = new HashMap<>();
        formData.put("client_id", this.instragramAPIProperties.clientId());
        formData.put("client_secret", this.instragramAPIProperties.clientSecret());
        formData.put("grant_type", "authorization_code");
        formData.put("redirect_uri", this.instragramAPIProperties.redirectUri());
        formData.put("code", "code");
        given(instragramAPIProperties.clientId()).willReturn("client_id");
        given(instragramAPIProperties.clientSecret()).willReturn("sercret");
        given(instragramAPIProperties.redirectUri()).willReturn("http://localhost:8080/test");
        given(accessTokenFirestoreRepository.get()).willReturn(AccessToken.builder().accessToken("accessToken").expireIn(100000000).tokenType("Baerer").build());
    }

    @Test
    void getShortLivedAccessToken_shouldReturnShortLivedAccessToken() {
        //given

        String accessToken = "ACCESS_TOKEN";
        stubFor(post(urlEqualTo("/oauth/access_token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\""+accessToken+"\",\"user_id\":\"01\"}")));

        //when
        ShortLivedAccessTokenDTO shortLivedAccessTokenDTO = apiInstagram.getShortLivedAccessToken(formData);
        //assert
        assertEquals(accessToken, shortLivedAccessTokenDTO.accessToken());
    }

    @Test
    void getShortLivedAccessToken_withErrorAPI400_shouldIGAPIOAuthException() {
        //given

        String errorMessage = "error message";
        String errorType = "OAuthException";
        String json = "{\"error_message\":\""+errorMessage+"\",\"error_type\":\""+errorType+"\"}";
        stubFor(post(urlEqualTo("/oauth/access_token"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)));



        //assert
        Exception e = assertThrows(IGAPIOAuthException.class, () -> {
            //when
            apiInstagram.getShortLivedAccessToken(formData);
        });

        assertEquals(errorMessage, e.getMessage());
    }

    @Test
    void getShortLivedAccessToken_withOtherErrorAPI_shouldIGAPIOAuthException() {
        //given

        String errorMessage = "error message";
        String errorType = "Other error";
        String json = "{\"error_message\":\""+errorMessage+"\",\"error_type\":\""+errorType+"\"}";
        stubFor(post(urlEqualTo("/oauth/access_token"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)));



        //assert
        Exception e = assertThrows(InstagramAPIException.class, () -> {
            //when
            apiInstagram.getShortLivedAccessToken(formData);
        });

        assertEquals("Unknown error calling Instagram API.", e.getMessage());
    }
}