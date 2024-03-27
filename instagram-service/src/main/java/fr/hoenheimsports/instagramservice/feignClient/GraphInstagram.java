package fr.hoenheimsports.instagramservice.feignClient;

import fr.hoenheimsports.instagramservice.config.FeignGraphConfig;
import fr.hoenheimsports.instagramservice.feignClient.dto.LongLivedAccessTokenDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserMediasDTO;
import fr.hoenheimsports.instagramservice.feignClient.dto.UserProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "instagram-graph", configuration = FeignGraphConfig.class)
public interface GraphInstagram {

    @GetMapping("/me")
    UserProfileDTO getMe(@RequestParam("fields") String fields, @RequestParam("access_token") String accessToken);

    @GetMapping("/{userId}/media")
    UserMediasDTO getAllMediaByUser(@PathVariable String userId, @RequestParam("fields") String fields, @RequestParam("access_token") String accessToken);

    @GetMapping("/{mediaId}/children")
    UserMediasDTO getChildrenMediaByMediaId(@PathVariable String mediaId, @RequestParam("fields") String fields, @RequestParam("access_token") String accessToken);

    //@GetMapping("/{mediaId}")
    //MediaDTO getMediaById(@PathVariable String mediaId, @RequestParam("fields") String fields, @RequestParam("access_token") String accessToken);
    @GetMapping("/access_token")
    LongLivedAccessTokenDTO getLongLivedAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("access_token") String accessToken);

    @GetMapping("/refresh_access_token")
    LongLivedAccessTokenDTO refreshAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("access_token") String accessToken);
}
