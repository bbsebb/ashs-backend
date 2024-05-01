package fr.hoenheimsports.instagramservice.feignClient;

import fr.hoenheimsports.instagramservice.config.InstragramAPIProperties;
import fr.hoenheimsports.instagramservice.feignClient.dto.LongLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserMediasDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserProfileDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 8090)
class GraphInstagramTest {
    @Autowired
    private GraphInstagram graphInstagram;

    @MockBean
    private InstragramAPIProperties instagramAPIProperties;

    @BeforeEach
    void setUp() {

        // Utilisation de given pour une configuration cohérente avec BDD Mockito
        given(instagramAPIProperties.clientId()).willReturn("client_id");
        given(instagramAPIProperties.clientSecret()).willReturn("secret");
        given(instagramAPIProperties.redirectUri()).willReturn("http://localhost:8080/test");
    }

    @Test
    void getMe_shouldReturnUserProfile() {
        // given
        String expectedId = "123";
        String expectedUsername = "john_doe";
        int expectedMediaCount = 100;
        String expectedAccountType = "personal";
        String fields = "id,username,media_count";
        String accessToken = "validToken";

        // JSON de réponse simulé
        String responseJson = String.format("{ \"id\": \"%s\", \"username\": \"%s\", \"media_count\": %d, \"account_type\": \"%s\" }",
                expectedId, expectedUsername, expectedMediaCount, expectedAccountType);

        // Configuration de WireMock pour simuler la réponse
        stubFor(get(urlPathEqualTo("/me"))
                .withQueryParam("fields", equalTo(fields))
                .withQueryParam("access_token", equalTo(accessToken))
                .willReturn(okJson(responseJson)));

        // When
        UserProfileDTO result = graphInstagram.getMe(fields, accessToken);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(expectedId);
        assertThat(result.username()).isEqualTo(expectedUsername);
        assertThat(result.mediaCount()).isEqualTo(expectedMediaCount);
        assertThat(result.accountType()).isEqualTo(expectedAccountType);
    }

    @Test
    void getAllMediaByUser_shouldReturnUserMedias() {
        String userId = "user123";
        String fields = "id,caption,media_type,media_url";
        String accessToken = "validToken";
        String responseJson = "{\"data\":[{\"id\":\"456\",\"caption\":\"A photo\",\"media_type\":\"IMAGE\",\"media_url\":\"http://example.com/photo.jpg\"}]}";

        stubFor(get(urlPathEqualTo("/" + userId + "/media"))
                .withQueryParam("fields", equalTo(fields))
                .withQueryParam("access_token", equalTo(accessToken))
                .willReturn(okJson(responseJson)));

        UserMediasDTO result = graphInstagram.getAllMediaByUser(userId, fields, accessToken);

        assertThat(result.data()).hasSize(1);
        assertThat(result.data().getFirst().id()).isEqualTo("456");
    }

    @Test
    void getChildrenMediaByMediaId_shouldReturnUserMedias() {
        String mediaId = "media123";
        String fields = "id,caption,media_type,media_url";
        String accessToken = "validToken";
        String responseJson = "{\"data\":[{\"id\":\"789\",\"caption\":\"Child photo\",\"media_type\":\"IMAGE\",\"media_url\":\"http://example.com/child.jpg\"}]}";

        stubFor(get(urlPathEqualTo("/" + mediaId + "/children"))
                .withQueryParam("fields", equalTo(fields))
                .withQueryParam("access_token", equalTo(accessToken))
                .willReturn(okJson(responseJson)));

        UserMediasDTO result = graphInstagram.getChildrenMediaByMediaId(mediaId, fields, accessToken);

        assertThat(result.data()).hasSize(1);
        assertThat(result.data().getFirst().id()).isEqualTo("789");
    }

    @Test
    void getLongLivedAccessToken_shouldReturnLongLivedToken() {
        String grantType = "ig_exchange_token";
        String clientSecret = "secret";
        String accessToken = "shortLivedToken";
        String responseJson = "{\"access_token\":\"longLivedAccessToken\",\"expires_in\":5184000,\"token_type\":\"bearer\"}";

        stubFor(get(urlPathEqualTo("/access_token"))
                .withQueryParam("grant_type", equalTo(grantType))
                .withQueryParam("client_secret", equalTo(clientSecret))
                .withQueryParam("access_token", equalTo(accessToken))
                .willReturn(okJson(responseJson)));

        LongLivedAccessTokenDTO result = graphInstagram.getLongLivedAccessToken(grantType, clientSecret, accessToken);

        assertThat(result.accessToken()).isEqualTo("longLivedAccessToken");
        assertThat(result.expiresIn()).isEqualTo(5184000);
    }

    @Test
    void refreshAccessToken_shouldReturnRefreshedToken() {
        String grantType = "ig_refresh_token";
        String accessToken = "oldAccessToken";
        String responseJson = "{\"access_token\":\"refreshedAccessToken\",\"expires_in\":5184000,\"token_type\":\"bearer\"}";

        stubFor(get(urlPathEqualTo("/refresh_access_token"))
                .withQueryParam("grant_type", equalTo(grantType))
                .withQueryParam("access_token", equalTo(accessToken))
                .willReturn(okJson(responseJson)));

        LongLivedAccessTokenDTO result = graphInstagram.refreshAccessToken(grantType, accessToken);

        assertThat(result.accessToken()).isEqualTo("refreshedAccessToken");
        assertThat(result.expiresIn()).isEqualTo(5184000);
    }

}