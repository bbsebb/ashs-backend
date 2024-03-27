package fr.hoenheimsports.instagramservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import fr.hoenheimsports.instagramservice.feignClient.InstagramGraphErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignGraphConfig {
    private final ObjectMapper objectMapper;


    public FeignGraphConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean(name = "graphErrorDecoder")
    public ErrorDecoder errorDecoder() {
        return new InstagramGraphErrorDecoder(objectMapper);
    }
}
