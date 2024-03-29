package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.config.InstragramAPIProperties;
import fr.hoenheimsports.instagramservice.feignClient.ApiInstagram;
import fr.hoenheimsports.instagramservice.feignClient.GraphInstagram;
import fr.hoenheimsports.instagramservice.feignClient.dto.LongLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.repositories.AccessTokenRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthInstagramServiceImpl implements AuthInstagramService{


    private final ApiInstagram apiInstagram;
    private final GraphInstagram graphInstagram;
    private final InstragramAPIProperties instragramAPIProperties;

    private final AccessTokenRepository accessTokenRepository;

    public AuthInstagramServiceImpl(ApiInstagram apiInstagram, GraphInstagram graphInstagram, InstragramAPIProperties instragramAPIProperties, AccessTokenRepository accessTokenRepository) {
        this.apiInstagram = apiInstagram;
        this.graphInstagram = graphInstagram;
        this.instragramAPIProperties = instragramAPIProperties;
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public void auth(String code) {

        String shortLivedAccessToken = this.getShortLivedAccessToken(code);
        LongLivedAccessTokenDTO longLivedAccessTokenDTO = this.graphInstagram.getLongLivedAccessToken( "ig_exchange_token", instragramAPIProperties.clientSecret(), shortLivedAccessToken);
        this.accessTokenRepository.save(AccessToken.builder().accessToken(longLivedAccessTokenDTO.accessToken()).expireIn(longLivedAccessTokenDTO.expiresIn()).tokenType(longLivedAccessTokenDTO.tokenType()).build());
    }

    @Override
    public AccessToken getAccessToken() {
        return this.accessTokenRepository.get();
    }

    @Override
    public void refreshAccessToken() {
        AccessToken accessToken = this.accessTokenRepository.get();
        String refreshedAccessToken = this.graphInstagram.refreshAccessToken("ig_refresh_token", accessToken.getAccessToken()).accessToken();
        this.accessTokenRepository.save(AccessToken.builder().accessToken(refreshedAccessToken).createdAt(accessToken.getCreatedAt()).expiresAt(accessToken.getExpiresAt()).tokenType(accessToken.getTokenType()).build());

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
