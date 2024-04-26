package fr.hoenheimsports.instagramservice.controllers;

import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.services.AuthInstagramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiInstagramController {

    private final AuthInstagramService authInstagramService;



    public ApiInstagramController( AuthInstagramService authInstagramService) {
        this.authInstagramService = authInstagramService;
    }

    @GetMapping("")
    public ResponseEntity<Void> auth(@RequestParam String code) {

        this.authInstagramService.auth(code);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/access-token/refresh")
    public ResponseEntity<Void> LongLivedAccessTokenDTO() {
        this.authInstagramService.refreshAccessToken();
        return ResponseEntity.noContent().build();
    }
}
