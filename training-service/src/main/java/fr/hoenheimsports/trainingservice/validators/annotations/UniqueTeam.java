package fr.hoenheimsports.trainingservice.validators.annotations;

import fr.hoenheimsports.trainingservice.validators.UniqueTeamValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueTeamValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTeam {
    String message() default "Cette équipe existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
