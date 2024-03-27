package fr.hoenheimsports.instagramservice.repositories;

import fr.hoenheimsports.instagramservice.models.AccessToken;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository {
    void save(AccessToken accessToken);
    AccessToken get();
}
