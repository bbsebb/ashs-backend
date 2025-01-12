package fr.hoenheimsports.trainingservice.services;

public interface UserSecurityService {
    boolean isAuthenticated();

    boolean hasRole(String role);
}
