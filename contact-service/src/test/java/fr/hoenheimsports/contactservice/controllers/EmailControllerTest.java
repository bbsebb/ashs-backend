package fr.hoenheimsports.contactservice.controllers;

import fr.hoenheimsports.contactservice.services.EmailService;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
@WebMvcTest(EmailController.class)
@ExtendWith(SpringExtension.class)
class EmailControllerTest {

    @MockBean
    private EmailService emailService;


    @Autowired
    private MockMvc mockMvc;



    @Test
    public void sendEmail_ShouldReturnStatusCreated() throws Exception {
        // Arrange
        String emailJson = """
                           {
                               "name": "Test Name",
                               "email": "test@example.com",
                               "message": "Hello, this is a test message"
                           }
                           """;
        Mockito.doNothing().when(emailService).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void sendEmail_MissingFieldMessage_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String emailJsonMissingField = """
                                        {
                                            "name": "Test Name",
                                            "email": "test@example.com"
                                        }
                                        """;

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailJsonMissingField))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void sendEmail_MissingFieldEmail_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String emailJsonMissingField = """
                                        {
                                            "name": "Test Name",
                                            "message": "Hello, this is a test message"
                                        }
                                        """;

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailJsonMissingField))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void sendEmail_MissingFieldName_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String emailJsonMissingField = """
                                        {
                                            "email": "test@example.com"
                                            "message": "Hello, this is a test message"
                                        }
                                        """;

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailJsonMissingField))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void sendEmail_IncorrectDataType_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String emailJsonIncorrectDataType = """
                                             {
                                                 "name": {"firstName": "Test", "lastName": "Name"},
                                                 "email": "test@example.com",
                                                 "message": "Hello, this is a test message"
                                             }
                                             """;

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailJsonIncorrectDataType))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void sendEmail_MalformedJson_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String malformedEmailJson = """
                                     {
                                         "name": "Test Name",
                                         "email": "test@example.com",
                                         "message": "Hello, this is a test message"
                                     """; // Notice the missing closing brace

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedEmailJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenNameIsEmpty_thenBadRequest() throws Exception {
        String emailJson = """
                           {
                               "name": "",
                               "email": "test@example.com",
                               "message": "Hello, this is a test message"
                           }
                           """;

        mockMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenEmailIsInvalid_thenBadRequest() throws Exception {
        String emailJson = """
                           {
                               "name": "Test Name",
                               "email": "not-a-valid-email",
                               "message": "Hello, this is a test message"
                           }
                           """;

        mockMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenMessageIsEmpty_thenBadRequest() throws Exception {
        String emailJson = """
                           {
                               "name": "Test Name",
                               "email": "test@example.com",
                               "message": ""
                           }
                           """;

        mockMvc.perform(MockMvcRequestBuilders.post("/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emailJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}