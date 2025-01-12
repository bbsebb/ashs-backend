package fr.hoenheimsports.trainingservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/security")
public class SecurityController {



    @GetMapping
    public ResponseEntity<?> security(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("username", "user"));
    }
}
