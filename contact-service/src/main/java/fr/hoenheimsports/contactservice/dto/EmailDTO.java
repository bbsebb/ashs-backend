package fr.hoenheimsports.contactservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(
        @NotBlank(message = "Name cannot be empty")
        String name,
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email cannot be empty")
        String email,
        @NotBlank(message = "Message cannot be empty")
        String message) {
}
