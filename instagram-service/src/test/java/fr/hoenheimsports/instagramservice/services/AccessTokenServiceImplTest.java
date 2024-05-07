package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.repositories.AccessTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccessTokenServiceImplTest {

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @InjectMocks
    private AccessTokenServiceImpl accessTokenService;



    @Test
    public void getReturnsAccessTokenWhenExists() {
        AccessToken accessToken = new AccessToken();
        given(accessTokenRepository.findById("singletonToken")).willReturn(Optional.of(accessToken));

        Optional<AccessToken> result = accessTokenService.get();

        assertThat(result).isPresent().containsSame(accessToken);
    }

    @Test
    public void getReturnsEmptyWhenNoAccessToken() {
        given(accessTokenRepository.findById("singletonToken")).willReturn(Optional.empty());

        Optional<AccessToken> result = accessTokenService.get();

        assertThat(result).isNotPresent();
    }

    @Test
    public void savePersistsAccessToken() {
        AccessToken accessToken = new AccessToken();

        accessTokenService.save(accessToken);

        verify(accessTokenRepository).save(accessToken);
        assertThat(accessToken.getId()).isEqualTo("singletonToken");
    }
}