package com.example.notification_service.services;

import com.example.notification_service.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.website-url}")
    private String websiteUrl;

    // Метод для отправки письма при создании аккаунта
    public void sendAccountCreationEmail(UserEvent userEvent) {
        String subject = "Добро пожаловать!";
        String text = String.format(
                "Здравствуйте, Ваш аккаунт на сайте %s был успешно создан.",
                websiteUrl
        );
        sendSimpleMessage(userEvent.getEmail(), subject, text);
    }

    // Метод для отправки письма при удалении аккаунта
    public void sendAccountDeletionEmail(UserEvent userEvent) {
        String subject = "Ваш аккаунт был удален";
        String text = "Здравствуйте! Ваш аккаунт был удалён.";
        sendSimpleMessage(userEvent.getEmail(), subject, text);
    }

    // Вспомогательный метод для отправки простого текстового письма
    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Garik24071989@gmail.com"); // Должен совпадать с spring.mail.username
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        // Для логирования
        System.out.println("Email sent to: " + to + " with subject: " + subject);
    }
}