package fr.hoenheimsports.instagramservice.repositories;

import fr.hoenheimsports.instagramservice.models.AccessToken;
import org.springframework.stereotype.Repository;

@Repository
public class AccessTokenInMemoryRepositoryImpl implements AccessTokenRepository{
    private AccessToken AccessToken = null;


    @Override
    public void save(AccessToken accessToken) {
        this.AccessToken = accessToken;
    }

    @Override
    public AccessToken get() {
        return this.AccessToken;
    }
}
