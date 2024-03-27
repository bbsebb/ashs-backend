package fr.hoenheimsports.instagramservice.controllers;

import fr.hoenheimsports.instagramservice.models.Media;
import fr.hoenheimsports.instagramservice.services.GraphInstagramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GraphInstagramController {


    private final GraphInstagramService graphInstagramService;

    public GraphInstagramController( GraphInstagramService graphInstagramService) {
        this.graphInstagramService = graphInstagramService;
    }


    @GetMapping("/media")
    public ResponseEntity<List<Media>> getMedias() {
        return ResponseEntity.status(HttpStatus.OK).body(this.graphInstagramService.getMedias());
    }

}
