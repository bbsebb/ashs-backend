package fr.hoenheimsports.instagramservice.feignClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import fr.hoenheimsports.instagramservice.exceptions.*;
import fr.hoenheimsports.instagramservice.feignClient.dto.GraphErrorResponse;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class InstagramGraphErrorDecoder implements ErrorDecoder{
    private final ObjectMapper objectMapper;


    public InstagramGraphErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.body() != null) {
            try(Reader reader = response.body().asReader(StandardCharsets.UTF_8)) {
                GraphErrorResponse errorResponse = objectMapper.readValue(reader, GraphErrorResponse.class);
                return switch (errorResponse.error().type()) {
                    case "IGApiBadRequestException" -> new IGAPIBadRequestException(errorResponse.error().message());
                    case "IGApiException" -> new IGAPIException(errorResponse.error().message());
                    case "IGApiUnauthorizedException" -> new IGAPIUnauthorizedException(errorResponse.error().message());
                    case "CodedException" -> new CodedException(errorResponse.error().message());
                    case "IGApiForbiddenException" -> new IGAPIForbiddenException(errorResponse.error().message());
                    default -> new InstagramAPIException("Unknown error calling Instagram API.");

                };
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new InstagramAPIException("Erreur inconnue lors de l'appel à l'API Instagram.");
    }

}
