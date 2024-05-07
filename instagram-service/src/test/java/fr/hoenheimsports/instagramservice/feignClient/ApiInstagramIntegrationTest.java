package fr.hoenheimsports.instagramservice.feignClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import fr.hoenheimsports.instagramservice.config.InstragramAPIProperties;
import fr.hoenheimsports.instagramservice.exceptions.IGAPIOAuthException;
import fr.hoenheimsports.instagramservice.exceptions.InstagramAPIException;
import fr.hoenheimsports.instagramservice.feignClient.dto.ShortLivedAccessTokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 8090)
public class ApiInstagramIntegrationTest {

    @Autowired
    private ApiInstagram apiInstagram;
    @MockBean
    private InstragramAPIProperties instagramAPIProperties;

    private Map<String, String> formData;

    @BeforeEach
    void setUp() {
        formData = new HashMap<>();
        formData.put("client_id", "client_id");
        formData.put("client_secret", "secret");
        formData.put("grant_type", "authorization_code");
        formData.put("redirect_uri", "http://localhost:8080/test");
        formData.put("code", "code");

        // Utilisation de given pour une configuration cohérente avec BDD Mockito
        given(instagramAPIProperties.clientId()).willReturn("client_id");
        given(instagramAPIProperties.clientSecret()).willReturn("secret");
        given(instagramAPIProperties.redirectUri()).willReturn("http://localhost:8080/test");
    }

    @Test
    void getShortLivedAccessToken_shouldReturnShortLivedAccessToken() {
        String accessToken = "ACCESS_TOKEN";
        stubFor(post(urlEqualTo("/oauth/access_token"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"access_token\":\"" + accessToken + "\",\"user_id\":\"01\"}")));

        ShortLivedAccessTokenDTO shortLivedAccessTokenDTO = apiInstagram.getShortLivedAccessToken(formData);

        assertThat(shortLivedAccessTokenDTO.accessToken()).isEqualTo(accessToken);
    }

    @Test
    void getShortLivedAccessToken_withErrorAPI400_shouldThrowIGAPIOAuthException() {
        String errorMessage = "error message";
        stubFor(post(urlEqualTo("/oauth/access_token"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error_message\":\"" + errorMessage + "\",\"error_type\":\"OAuthException\"}")));

        Throwable thrown = catchThrowable(() -> apiInstagram.getShortLivedAccessToken(formData));

        assertThat(thrown).isInstanceOf(IGAPIOAuthException.class)
                .hasMessageContaining(errorMessage);
    }

    @Test
    void getShortLivedAccessToken_withOtherErrorAPI_shouldThrowInstagramAPIException() {
        String errorMessage = "error message";
        stubFor(post(urlEqualTo("/oauth/access_token"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error_message\":\"" + errorMessage + "\",\"error_type\":\"Other error\"}")));

        Throwable thrown = catchThrowable(() -> apiInstagram.getShortLivedAccessToken(formData));

        assertThat(thrown).isInstanceOf(InstagramAPIException.class)
                .hasMessageContaining("Unknown error calling Instagram API.");
    }
}
