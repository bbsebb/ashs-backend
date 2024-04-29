package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.models.AccessToken;

public interface AuthInstagramService {
    void auth(String code);
    void refreshAccessToken();
    AccessToken getAccessToken();


}
