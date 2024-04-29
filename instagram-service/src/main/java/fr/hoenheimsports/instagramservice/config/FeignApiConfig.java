package fr.hoenheimsports.instagramservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import fr.hoenheimsports.instagramservice.feignClient.InstagramApiErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignApiConfig {


    private final ObjectMapper objectMapper;

    public FeignApiConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean(name = "apiErrorDecoder")
    public ErrorDecoder errorDecoder() {
        return new InstagramApiErrorDecoder(objectMapper);
    }

    @Bean
    public Encoder encoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }


}
