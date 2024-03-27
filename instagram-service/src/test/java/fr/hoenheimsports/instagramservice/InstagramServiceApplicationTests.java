package fr.hoenheimsports.instagramservice;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class InstagramServiceApplicationTests {

    @MockBean
    private Firestore firestore;
    @MockBean
    private FirebaseApp firebaseApp;
    @MockBean
    private GoogleCredentials googleCredentials;

    @Test
    void contextLoads() {

    }

}
