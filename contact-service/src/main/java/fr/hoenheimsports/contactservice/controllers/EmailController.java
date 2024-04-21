package fr.hoenheimsports.contactservice.controllers;

import fr.hoenheimsports.contactservice.dto.EmailDTO;
import fr.hoenheimsports.contactservice.exceptions.InvalidEmailParameterException;
import fr.hoenheimsports.contactservice.services.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid EmailDTO emailDTO) throws InvalidEmailParameterException {
        emailService.sendEmail(emailDTO.name(), emailDTO.email(), emailDTO.message());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
