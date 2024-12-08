package fr.hoenheimsports.trainingservice.Exception;

import fr.hoenheimsports.trainingservice.dto.ApiErrorDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        // Collecte des erreurs de champ
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        // Collecte des erreurs globales
        details.addAll(
                ex.getBindingResult()
                        .getGlobalErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList()
        );

        // Construction de l'ApiErrorDTO
        ApiErrorDTO errorResponse = new ApiErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getDescription(false),
                details
        );

        // Retourne la réponse avec le statut 400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericException(Exception ex, WebRequest request) {
        ApiErrorDTO errorResponse = new ApiErrorDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false),
                List.of("Une erreur est survenue")
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

