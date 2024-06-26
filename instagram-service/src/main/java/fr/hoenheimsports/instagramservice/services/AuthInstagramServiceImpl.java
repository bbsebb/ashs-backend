package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.config.InstragramAPIProperties;
import fr.hoenheimsports.instagramservice.exceptions.AccessTokenNotFound;
import fr.hoenheimsports.instagramservice.feignClient.ApiInstagram;
import fr.hoenheimsports.instagramservice.feignClient.GraphInstagram;
import fr.hoenheimsports.instagramservice.feignClient.dto.LongLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.models.AccessToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthInstagramServiceImpl implements AuthInstagramService{


    private final ApiInstagram apiInstagram;
    private final GraphInstagram graphInstagram;
    private final InstragramAPIProperties instragramAPIProperties;
    private final AccessTokenService accessTokenService;

    public AuthInstagramServiceImpl(ApiInstagram apiInstagram, GraphInstagram graphInstagram, InstragramAPIProperties instragramAPIProperties, AccessTokenService accessTokenService) {
        this.apiInstagram = apiInstagram;
        this.graphInstagram = graphInstagram;
        this.instragramAPIProperties = instragramAPIProperties;
        this.accessTokenService = accessTokenService;
    }

    @Override
    public void getAccessToken(String code) {
        String accessToken = this.getShortLivedAccessToken(code);
        LongLivedAccessTokenDTO longLivedAccessTokenDTO = this.graphInstagram.getLongLivedAccessToken( "ig_exchange_token", instragramAPIProperties.clientSecret(), accessToken);
        this.saveAccessToken(longLivedAccessTokenDTO);
    }

    @Override
    public AccessToken retrieveAccessToken() {
        return this.accessTokenService.get().orElseThrow(AccessTokenNotFound::new);
    }

    @Override
    public void refreshAccessToken() {
        String accessToken = this.retrieveAccessToken().getAccessToken();
        LongLivedAccessTokenDTO refreshedAccessToken = this.graphInstagram.refreshAccessToken("ig_refresh_token",accessToken);
        this.saveAccessToken(refreshedAccessToken);
    }

    private void saveAccessToken(LongLivedAccessTokenDTO refreshedAccessToken){
        var createAt = LocalDateTime.now();
        var expireAt = createAt.plusSeconds(refreshedAccessToken.expiresIn());
        this.accessTokenService.save(
                AccessToken.builder()
                        .accessToken(refreshedAccessToken.accessToken())
                        .createdAt(createAt)
                        .expiresAt(expireAt)
                        .tokenType(refreshedAccessToken.tokenType())
                        .build());
    }




    private String getShortLivedAccessToken(String code) {
        Map<String, String> formData = new HashMap<>();
        formData.put("client_id", this.instragramAPIProperties.clientId());
        formData.put("client_secret", this.instragramAPIProperties.clientSecret());
        formData.put("grant_type", "authorization_code");
        formData.put("redirect_uri", this.instragramAPIProperties.redirectUri());
        formData.put("code", code);
        System.out.println(formData);
        return this.apiInstagram.getShortLivedAccessToken(formData).accessToken();
    }

}
