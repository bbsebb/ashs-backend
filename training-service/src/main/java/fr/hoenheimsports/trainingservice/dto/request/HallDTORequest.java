package fr.hoenheimsports.trainingservice.dto.request;

import fr.hoenheimsports.trainingservice.models.Hall;
import fr.hoenheimsports.trainingservice.validators.OnCreate;
import fr.hoenheimsports.trainingservice.validators.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Hall}
 */

public record HallDTORequest(Long id,
                             @NotBlank(message = "le nom de la salle ne peut pas être vide",groups = {OnCreate.class}) @Size(max = 100, message = "la taille maximum du nom de la salle est de 100 caractères",groups = {OnCreate.class, OnUpdate.class}) String name,
                             @Valid AddressDTORequest address) implements Serializable {
}