package fr.hoenheimsports.trainingservice.validators;

import fr.hoenheimsports.trainingservice.dto.request.TeamDTORequest;
import fr.hoenheimsports.trainingservice.repositories.TeamRepository;
import fr.hoenheimsports.trainingservice.validators.annotations.UniqueTeam;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueTeamValidator implements ConstraintValidator<UniqueTeam, TeamDTORequest> {


    private final TeamRepository teamRepository;

    public UniqueTeamValidator(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public boolean isValid(TeamDTORequest teamDtoRequest, ConstraintValidatorContext context) {
        if (teamDtoRequest == null) {
            return true; // Null values are not checked
        }

        if (teamDtoRequest.id() != null) { // Vérification si un ID est fourni
            return teamRepository.findById(teamDtoRequest.id())
                    .map(existingTeam ->
                            existingTeam.getGender().equals(teamDtoRequest.gender()) &&
                                    existingTeam.getCategory().equals(teamDtoRequest.category()) &&
                                    existingTeam.getTeamNumber()== teamDtoRequest.teamNumber())
                    .orElse(false); // Si l'ID est invalide, l'objet est considéré comme non valide
        }

        return !teamRepository.existsByGenderAndCategoryAndTeamNumber(
                teamDtoRequest.gender(),
                teamDtoRequest.category(),
                teamDtoRequest.teamNumber()
        );
    }
}
