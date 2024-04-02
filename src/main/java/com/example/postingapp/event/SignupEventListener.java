package com.example.postingapp.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.postingapp.entity.User;
import com.example.postingapp.service.VerificationTokenService;

/*
 * This class listens for SignupEvent and sends an email to the user with a verification link.
 */
@Component
public class SignupEventListener {
    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender javaMailSender;

    public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;
        this.javaMailSender = mailSender;
    }

    @EventListener
    private void onSignupEvent(SignupEvent signupEvent) {

        // Create verification token
        User user = signupEvent.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.create(user, token);

        // Prepare email
        String recipientAddress = user.getEmail();
        String subject = "Email verification";
        String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
        String message = "Please verify your email address by clicking the link below: \n";

        // Send email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(message + "\n" + confirmationUrl);
        javaMailSender.send(mailMessage);
    }
}

