package fr.hoenheimsports.instagramservice.controllers;

import fr.hoenheimsports.instagramservice.services.AuthInstagramService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class ApiInstagramControllerTest {

    @Mock
    private AuthInstagramService authInstagramService;
    @InjectMocks
    private ApiInstagramController apiInstagramController;

    @Test
    public void authReturnsNoContentResponse() {
        // Arrange
        String code = "code";
        willDoNothing().given(authInstagramService).getAccessToken(code);

        // Act
        ResponseEntity<Void> result = apiInstagramController.auth(code);

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void refreshAccessTokenReturnsNoContentResponse() {
        // Arrange
        willDoNothing().given(authInstagramService).refreshAccessToken();

        // Act
        ResponseEntity<Void> result = apiInstagramController.LongLivedAccessTokenDTO();

        // Assert
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}