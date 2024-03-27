package fr.hoenheimsports.instagramservice.exceptions;

public class IGAPIUnauthorizedException extends InstagramAPIException{
    public IGAPIUnauthorizedException(String message) {
        super(message);
    }
}
