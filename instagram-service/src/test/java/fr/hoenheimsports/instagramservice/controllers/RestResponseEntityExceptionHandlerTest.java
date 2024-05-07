package fr.hoenheimsports.instagramservice.controllers;

import fr.hoenheimsports.instagramservice.exceptions.InstagramAPIException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestResponseEntityExceptionHandlerTest {

    @Test
    public void testInstagramAPIExceptionHandling() throws Exception {
        // Créer un MockMvc avec un contrôleur fictif qui lance l'exception
        // Assurez-vous que votre gestionnaire d'exception est utilisé
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
                .setControllerAdvice(new RestResponseEntityExceptionHandler()) // Assurez-vous que votre gestionnaire d'exception est utilisé
                .build();

        // Effectuer une requête HTTP et vérifier le résultat
        mockMvc.perform(get("/test/instagram-exception"))
                .andExpect(status().isBadGateway())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(content().json("{\"status\":502,\"detail\":\"Error caused by Instagram API\"}"));
    }

    // Contrôleur fictif pour le test
    @RestController
    static class TestController {
        @GetMapping("/test/instagram-exception")
        public void throwInstagramAPIException() {
            throw new InstagramAPIException("Error caused by Instagram API");
        }
    }
}
