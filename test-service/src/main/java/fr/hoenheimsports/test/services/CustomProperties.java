package fr.hoenheimsports.test.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomProperties {

    @Value("${custom.message}")
    private String customMessage;

    public String getCustomMessage() {
        return customMessage;
    }
}
