package fr.hoenheimsports.instagramservice.controllers;

import fr.hoenheimsports.instagramservice.models.AccessToken;
import fr.hoenheimsports.instagramservice.services.AuthInstagramService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiInstagramController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ApiInstagramControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthInstagramService authInstagramService;


    @Test
    public void auth_returnGoodStatus() throws Exception {
        String code = "some_code";
        mockMvc.perform(get("/api/auth")
                        .param("code", code))
                .andExpect(status().isNoContent());

    }
    @Test
    void testGetLongLivedAccessToken() throws Exception {
        AccessToken accessToken = AccessToken.builder()
                .accessToken("some_access_token")
                .expireIn(3600)
                .tokenType("Bearer")
                .build();

        given(authInstagramService.getAccessToken()).willReturn(accessToken);

        mockMvc.perform(get("/api/auth/access-token/long-lived"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken", CoreMatchers.is(accessToken.getAccessToken())));
    }

    @Test
    void testLongLivedAccessTokenDTO() throws Exception {
        mockMvc.perform(get("/api/auth/access-token/refresh"))
                .andExpect(status().isNoContent());
    }

}