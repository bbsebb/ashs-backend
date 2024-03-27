package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.config.InstragramAPIProperties;
import fr.hoenheimsports.instagramservice.feignClient.ApiInstagram;
import fr.hoenheimsports.instagramservice.feignClient.GraphInstagram;
import fr.hoenheimsports.instagramservice.feignClient.dto.LongLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.ShortLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.repositories.AccessTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthInstagramServiceImplTest {

    @Mock
    private ApiInstagram apiInstagram;
    @Mock
    private GraphInstagram graphInstagram;
    @Mock
    private InstragramAPIProperties instragramAPIProperties;
    @Mock
    private AccessTokenRepository accessTokenRepository;

    @InjectMocks
    private AuthInstagramServiceImpl authInstagramService;



    @Test
    void authShouldSaveLongLivedAccessToken() {
        String code = "dummyCode";
        String shortLivedToken = "shortLivedToken";
        when(apiInstagram.getShortLivedAccessToken(anyMap())).thenReturn(new ShortLivedAccessTokenDTO(shortLivedToken, "userId"));
        when(graphInstagram.getLongLivedAccessToken(eq("ig_exchange_token"), anyString(), eq(shortLivedToken)))
                .thenReturn(new LongLivedAccessTokenDTO("longLivedToken", 5184000, "Bearer"));

        when(instragramAPIProperties.clientSecret()).thenReturn("secret");

        authInstagramService.auth(code);

        verify(accessTokenRepository).save(any(AccessToken.class));
    }

    @Test
    void getAccessTokenShouldReturnAccessToken() {
        AccessToken accessToken = AccessToken.builder()
                .accessToken("accessToken")
                .createdAt(LocalDateTime.now())
                .expireIn(3600)
                .build();

        when(accessTokenRepository.get()).thenReturn(accessToken);

        AccessToken result = authInstagramService.getAccessToken();

        verify(accessTokenRepository).get();
        assertNotNull(result);
        assertEquals("accessToken", result.getAccessToken());
    }

    @Test
    void refreshAccessTokenShouldUpdateToken() {
        AccessToken oldAccessToken = AccessToken.builder()
                .accessToken("oldAccessToken")
                .createdAt(LocalDateTime.now().minusDays(1))
                .expireIn(3600)
                .build();

        when(accessTokenRepository.get()).thenReturn(oldAccessToken);
        when(graphInstagram.refreshAccessToken(eq("ig_refresh_token"), anyString()))
                .thenReturn(new LongLivedAccessTokenDTO("refreshedAccessToken", 5184000, "Bearer"));

        authInstagramService.refreshAccessToken();

        verify(accessTokenRepository).save(any(AccessToken.class));
    }
}