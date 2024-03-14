package fr.hoenheimsports.test.controllers;

import fr.hoenheimsports.test.services.CustomProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final CustomProperties customProperties;

    public TestController(CustomProperties customProperties) {
        this.customProperties = customProperties;
    }

    @GetMapping("/custom")
    public String showCustomMessage() {
        return this.customProperties.getCustomMessage();
    }
}
