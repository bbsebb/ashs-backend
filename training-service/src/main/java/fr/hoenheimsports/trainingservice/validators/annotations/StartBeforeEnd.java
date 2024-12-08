package fr.hoenheimsports.trainingservice.validators.annotations;

import fr.hoenheimsports.trainingservice.validators.StartBeforeEndValidator;
import fr.hoenheimsports.trainingservice.validators.UniqueTeamValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEnd {
    String message() default "La date de début doit être avant la date de fin";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
