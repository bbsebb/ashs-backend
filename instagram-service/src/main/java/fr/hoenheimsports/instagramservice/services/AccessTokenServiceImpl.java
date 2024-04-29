package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.repositories.AccessTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    private static final String ID = "singletonToken";
    private final AccessTokenRepository accessTokenRepository;

    public AccessTokenServiceImpl(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }


    @Override
    public Optional<AccessToken> get() {
        return this.accessTokenRepository.findById(ID);
    }

    @Override
    public void save(AccessToken accessToken) {
        accessToken.setId(ID);
        this.accessTokenRepository.save(accessToken);
    }

}
