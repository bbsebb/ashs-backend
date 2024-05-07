package fr.hoenheimsports.instagramservice.services;

import fr.hoenheimsports.instagramservice.models.AccessToken;

public interface AuthInstagramService {
    void getAccessToken(String code);
    void refreshAccessToken();
    AccessToken retrieveAccessToken();


}
