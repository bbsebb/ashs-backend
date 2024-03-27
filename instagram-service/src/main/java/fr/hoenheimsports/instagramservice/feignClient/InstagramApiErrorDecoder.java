package fr.hoenheimsports.instagramservice.feignClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import fr.hoenheimsports.instagramservice.exceptions.IGAPIOAuthException;
import fr.hoenheimsports.instagramservice.exceptions.InstagramAPIException;
import fr.hoenheimsports.instagramservice.feignClient.dto.APIErrorResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Component
public class InstagramApiErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;

    public InstagramApiErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.body() != null) {
            try(Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
                APIErrorResponse errorResponse = objectMapper.readValue(reader, APIErrorResponse.class);
                if(errorResponse.errorType().equals("OAuthException")) {
                    return new IGAPIOAuthException(errorResponse.errorMessage());
                } else {
                    return new InstagramAPIException("Unknown error calling Instagram API.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new InstagramAPIException("Erreur inconnue lors de l'appel à l'API Instagram.");
    }


}
