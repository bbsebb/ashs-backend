package fr.hoenheimsports.instagramservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "instagram.api")
public record InstragramAPIProperties(String clientId, String clientSecret, String redirectUri) {
}
