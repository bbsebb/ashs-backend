package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.config.InstragramAPIProperties;
import fr.hoenheimsports.instagramservice.exceptions.AccessTokenNotFound;
import fr.hoenheimsports.instagramservice.feignClient.ApiInstagram;
import fr.hoenheimsports.instagramservice.feignClient.GraphInstagram;
import fr.hoenheimsports.instagramservice.feignClient.dto.LongLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.ShortLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.models.AccessToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthInstagramServiceImplTest {

    @Mock
    private ApiInstagram apiInstagram;

    @Mock
    private GraphInstagram graphInstagram;

    @Mock
    private InstragramAPIProperties instragramAPIProperties;

    @Mock
    private AccessTokenService accessTokenService;

    @InjectMocks
    private AuthInstagramServiceImpl authInstagramService;



    @Test
    public void getAccessTokenSavesLongLivedAccessToken() {
        // Données des propriétés API
        given(instragramAPIProperties.clientId()).willReturn("clientId");
        given(instragramAPIProperties.clientSecret()).willReturn("clientSecret");
        given(instragramAPIProperties.redirectUri()).willReturn("redirectUri");

        // Configuration des mocks
        ShortLivedAccessTokenDTO shortLivedAccessTokenDTO = new ShortLivedAccessTokenDTO("shortLivedToken","userID");
        given(apiInstagram.getShortLivedAccessToken(anyMap())).willReturn(shortLivedAccessTokenDTO);

        LongLivedAccessTokenDTO longLivedAccessTokenDTO = new LongLivedAccessTokenDTO("longLivedToken", 3600, "Bearer");
        given(graphInstagram.getLongLivedAccessToken("ig_exchange_token", "clientSecret", "shortLivedToken")).willReturn(longLivedAccessTokenDTO);

        // Action
        authInstagramService.getAccessToken("authCode");

        // Capture de l'argument passé à save()
        ArgumentCaptor<AccessToken> captor = ArgumentCaptor.forClass(AccessToken.class);
        then(accessTokenService).should().save(captor.capture());
        AccessToken savedAccessToken = captor.getValue();

        // Assertions
        assertThat(savedAccessToken.getAccessToken()).isEqualTo("longLivedToken");
        assertThat(savedAccessToken.getTokenType()).isEqualTo("Bearer");
        assertThat(savedAccessToken.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(savedAccessToken.getExpiresAt()).isCloseTo(LocalDateTime.now().plusSeconds(3600), within(1, ChronoUnit.SECONDS));

        // Vérifications des interactions
        then(apiInstagram).should().getShortLivedAccessToken(anyMap());
        then(graphInstagram).should().getLongLivedAccessToken("ig_exchange_token", "clientSecret", "shortLivedToken");

    }

    @Test
    public void retrieveAccessTokenReturnsAccessToken() {
        AccessToken accessToken = new AccessToken();
        given(accessTokenService.get()).willReturn(Optional.of(accessToken));

        AccessToken result = authInstagramService.retrieveAccessToken();

        assertThat(result).isSameAs(accessToken);
    }

    @Test
    public void retrieveAccessTokenThrowsWhenNoAccessToken() {
        given(accessTokenService.get()).willReturn(Optional.empty());

        assertThatThrownBy(() -> authInstagramService.retrieveAccessToken())
                .isInstanceOf(AccessTokenNotFound.class);
    }

    @Test
    public void refreshAccessTokenUpdatesAccessToken() {
        // Données pour le test
        AccessToken oldAccessToken = AccessToken.builder()
                .accessToken("oldToken")
                .createdAt(LocalDateTime.now().minusDays(1))
                .expiresAt(LocalDateTime.now().plusHours(1))
                .tokenType("Bearer")
                .build();
        given(accessTokenService.get()).willReturn(Optional.of(oldAccessToken));
        LongLivedAccessTokenDTO refreshedTokenDTO = new LongLivedAccessTokenDTO("newToken", 7200, "Bearer");
        given(graphInstagram.refreshAccessToken("ig_refresh_token", "oldToken")).willReturn(refreshedTokenDTO);

        // Action
        this.authInstagramService.refreshAccessToken();

        // Capture l'argument passé à save()
        ArgumentCaptor<AccessToken> captor = ArgumentCaptor.forClass(AccessToken.class);
        verify(accessTokenService).save(captor.capture());
        AccessToken savedAccessToken = captor.getValue();

        // Assertions
        assertThat(savedAccessToken.getAccessToken()).isEqualTo("newToken");
        assertThat(savedAccessToken.getTokenType()).isEqualTo("Bearer");
        assertThat(savedAccessToken.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        assertThat(savedAccessToken.getExpiresAt()).isCloseTo(LocalDateTime.now().plusSeconds(7200), within(1, ChronoUnit.SECONDS));

    }
}