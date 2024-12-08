package fr.hoenheimsports.trainingservice.validators.annotations;

import fr.hoenheimsports.trainingservice.validators.UniqueCoachValidator;
import fr.hoenheimsports.trainingservice.validators.UniqueTeamValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueCoachValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCoach {
    String message() default "Cet entraineur existe déjà";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
