package fr.hoenheimsports.instagramservice.controllers;

import fr.hoenheimsports.instagramservice.exceptions.InstagramAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InstagramAPIException.class)
    public ResponseEntity<ProblemDetail> handleInstagramAPIException(InstagramAPIException iae) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, iae.getMessage()));
    }

}
