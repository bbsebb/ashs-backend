package fr.hoenheimsports.contactservice.exceptions;

/**
 * Exception thrown when an invalid email parameter is passed to the email service.
 * The email parameter is invalid if it is null or blank.
 */
public class InvalidEmailParameterException extends Exception{
    public InvalidEmailParameterException(String message) {
        super(message);
    }

}
