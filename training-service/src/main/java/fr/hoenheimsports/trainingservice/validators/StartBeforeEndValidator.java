package fr.hoenheimsports.trainingservice.validators;

import fr.hoenheimsports.trainingservice.dto.request.TimeSlotDTORequest;
import fr.hoenheimsports.trainingservice.validators.annotations.StartBeforeEnd;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, TimeSlotDTORequest> {

    @Override
    public boolean isValid(TimeSlotDTORequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are not checked
        }

        return value.startTime().isBefore(value.endTime());
    }
}
