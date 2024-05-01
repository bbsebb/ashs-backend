package fr.hoenheimsports.instagramservice.feignClient;

import fr.hoenheimsports.instagramservice.config.FeignApiConfig;
import fr.hoenheimsports.instagramservice.feignClient.dto.ShortLivedAccessTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "instagram-api", configuration = FeignApiConfig.class)
public interface ApiInstagram {

    @PostMapping(value ="/oauth/access_token",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ShortLivedAccessTokenDTO getShortLivedAccessToken(@RequestBody Map<String, ?> formData);

}
