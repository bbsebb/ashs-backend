package fr.hoenheimsports.trainingservice.dto.request;

import fr.hoenheimsports.trainingservice.validators.OnCreate;
import fr.hoenheimsports.trainingservice.validators.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.Address}
 */
public record AddressDTORequest(
        @NotBlank(message = "L'adresse ne doit pas être vide",groups = {OnCreate.class})
        @Size(max = 100, message = "l'adresse doit être au maximum de 100 caractères",groups = {OnCreate.class, OnUpdate.class})
        String street,
        @NotBlank(message = "La ville ne doit pas être vide",groups = {OnCreate.class})
        @Size(max = 50, message = "la ville doit être au maximum de 50 caractères",groups = {OnCreate.class, OnUpdate.class})
        String city,
        @NotBlank(message = "Le code postal ne doit pas être vide",groups = {OnCreate.class})
        @Size(max = 10, message = "le code postal doit être au maximum de 10 caractères",groups = {OnCreate.class, OnUpdate.class})
        String postalCode,
        @NotBlank(message = "Le pays ne doit pas être vide",groups = {OnCreate.class})
        @Size(max = 50, message = "Le pays doit être au maximum de 50 caractères",groups = {OnCreate.class, OnUpdate.class})
        String country) implements Serializable {
}