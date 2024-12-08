package fr.hoenheimsports.trainingservice.validators;

import fr.hoenheimsports.trainingservice.dto.request.CoachDTORequest;
import fr.hoenheimsports.trainingservice.repositories.CoachRepository;
import fr.hoenheimsports.trainingservice.validators.annotations.UniqueCoach;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueCoachValidator implements ConstraintValidator<UniqueCoach, CoachDTORequest> {
    private final CoachRepository coachRepository;

    public UniqueCoachValidator(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    @Override
    public boolean isValid(CoachDTORequest value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }
        if (value.id() != null) { // Si un ID est présent, vérifier si l'email appartient au même coach
            return coachRepository.findById(value.id())
                    .map(existingCoach -> existingCoach.getEmail().equals(value.email()))
                    .orElse(false); // ID non trouvé, l'objet est invalide
        }

        return !this.coachRepository.existsCoachByEmail(value.email());
    }
}
