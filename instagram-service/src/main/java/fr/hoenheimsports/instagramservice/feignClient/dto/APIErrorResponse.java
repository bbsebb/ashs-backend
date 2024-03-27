package fr.hoenheimsports.instagramservice.feignClient.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record APIErrorResponse(String errorMessage, String errorType, int code) {
}
