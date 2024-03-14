package fr.hoenheimsports.contactservice.services;

import fr.hoenheimsports.contactservice.exceptions.InvalidEmailParameterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendEmail() throws InvalidEmailParameterException {
        // Setup
        String name = "John Doe";
        String email = "john@example.com";
        String message = "This is a test message";

        // Test

        emailService.sendEmail(name, email, message);

        // Verification
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        Mockito.verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(email, sentMessage.getFrom());
        assertEquals(email, sentMessage.getReplyTo());
        assertEquals("Notification de formulaire de contact de : " + name, sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains(name));
        assertTrue(sentMessage.getText().contains(email));
        assertTrue(sentMessage.getText().contains(message));
    }

    @Test
    public void whenNameIsNull_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail(null, "john@example.com", "This is a test message"));
    }

    @Test
    public void whenNameIsBlank_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail(" ", "john@example.com", "This is a test message"));
    }

    @Test
    public void whenEmailIsNull_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail("John Doe", null, "This is a test message"));
    }

    @Test
    public void whenEmailIsBlank_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail("John Doe", " ", "This is a test message"));
    }

    @Test
    public void whenMessageIsNull_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail("John Doe", "john@example.com", null));
    }

    @Test
    public void whenMessageIsBlank_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail("John Doe", "john@example.com", " "));
    }

    // Additional combination tests if needed

    @Test
    public void whenNameAndEmailAreNull_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail(null, null, "This is a test message"));
    }

    @Test
    public void whenEmailAndMessageAreBlank_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail("John Doe", " ", " "));
    }

    @Test
    public void whenNameAndMessageAreBlank_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail(" ", "john@example.com", " "));
    }

    // Test when all are null or blank
    @Test
    public void whenAllParametersAreNull_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail(null, null, null));
    }

    @Test
    public void whenAllParametersAreBlank_thenThrowException() {
        assertThrows(InvalidEmailParameterException.class, () -> emailService.sendEmail(" ", " ", " "));
    }
}