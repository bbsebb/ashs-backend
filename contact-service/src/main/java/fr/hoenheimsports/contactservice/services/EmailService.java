package fr.hoenheimsports.contactservice.services;

import fr.hoenheimsports.contactservice.exceptions.InvalidEmailParameterException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${contact.mail}")
    private String to;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String name,String email, String message) throws InvalidEmailParameterException {
        if (name == null || name.isBlank() || email == null || email.isBlank() || message == null || message.isBlank()) {
            throw new InvalidEmailParameterException("Name, email, and message must not be null or blank.");
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(email);
        mailMessage.setReplyTo(email);
        mailMessage.setTo(to);
        mailMessage.setSubject("Notification de formulaire de contact de : " + name );
        message = """
                Ce message a été envoyé depuis le formulaire de contact du site web.
                Nom : %s
                Email : %s
                Message : %s
                """.formatted(name, email, message);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
