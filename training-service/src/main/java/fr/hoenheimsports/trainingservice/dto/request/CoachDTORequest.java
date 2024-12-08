package fr.hoenheimsports.trainingservice.dto.request;

import fr.hoenheimsports.trainingservice.validators.OnCreate;
import fr.hoenheimsports.trainingservice.validators.OnUpdate;
import fr.hoenheimsports.trainingservice.validators.annotations.UniqueCoach;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.Coach}
 */
@UniqueCoach(message = "Un entraineur avec cet email existe déjà", groups = {OnCreate.class, OnUpdate.class})
public record CoachDTORequest(
        Long id,
        @Size(max = 100, message = "Le nom peut comporter 100 caractères maximum", groups = {OnCreate.class}) String name,
        @Size(max = 100, message = "Le prénom peut comporter 100 caractères maximum", groups = {OnCreate.class}) String surname,
        @NotBlank(message = "L'email ne peut pas être vide", groups = {OnCreate.class}) @Email(message = "L'email n'ai pas valide",groups = {OnCreate.class, OnUpdate.class})  String email,
        @NotBlank(message = "Le numéro de téléphone ne peut pas être vide", groups = {OnCreate.class}) @Size(max = 15, message = "Le numéro de téléphone peut comporter 15 caractères maximum",groups = {OnCreate.class, OnUpdate.class}) String phone) implements Serializable {
}