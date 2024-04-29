package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.models.AccessToken;

import java.util.Optional;


public interface AccessTokenService {
    Optional<AccessToken> get();
    void save(AccessToken accessToken);
}
