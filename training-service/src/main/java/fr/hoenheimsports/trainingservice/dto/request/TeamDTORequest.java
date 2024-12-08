package fr.hoenheimsports.trainingservice.dto.request;

import fr.hoenheimsports.trainingservice.models.Category;
import fr.hoenheimsports.trainingservice.models.Gender;
import fr.hoenheimsports.trainingservice.validators.OnCreate;
import fr.hoenheimsports.trainingservice.validators.OnUpdate;
import fr.hoenheimsports.trainingservice.validators.annotations.UniqueTeam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link fr.hoenheimsports.trainingservice.models.Team}
 */
@UniqueTeam(message = "Une équipe avec cette combinaison existe déjà", groups = {OnCreate.class,OnUpdate.class})
public record TeamDTORequest(Long id,
                             @NotNull(message = "Un genre est obligatoire", groups = {OnCreate.class}) Gender gender,
                             @NotNull(message = "Une catégorie est obligatoire", groups = {OnCreate.class}) Category category,
                             @Positive( message = "Une équipe doit avoir un numéro égale ou supérieure à 1", groups = {OnCreate.class, OnUpdate.class}) int teamNumber,
                             @Valid Set<CoachDTORequest> coaches,
                             @Valid Set<TrainingSessionDTORequest> trainingSessions) implements Serializable {
}