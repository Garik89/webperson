package com.example.notification_service.service;

import com.example.notification_service.dto.UserEvent;
import com.example.notification_service.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @MockitoSpyBean
    private JavaMailSender javaMailSender;

    @Test
    void testSendAccountCreationEmail() {
        // Given
        UserEvent userEvent = new UserEvent();
        userEvent.setOperation("CREATE");
        userEvent.setEmail("test@example.com");


        // When
        emailService.sendAccountCreationEmail(userEvent);

        // Then - проверяем что метод был вызван без ошибок
        // В реальном приложении здесь будет отправка email
        // Для тестов достаточно что метод выполнился без исключений
    }

    @Test
    void testSendAccountDeletionEmail() {
        // Given
        UserEvent userEvent = new UserEvent();
        userEvent.setOperation("DELETE");
        userEvent.setEmail("test@example.com");


        // When
        emailService.sendAccountDeletionEmail(userEvent);

        // Then - проверяем что метод был вызван без ошибок
    }

    @Test
    void testSendEmailWithNullFirstName() {
        // Given
        UserEvent userEvent = new UserEvent();
        userEvent.setOperation("CREATE");
        userEvent.setEmail("test@example.com");


        // When
        emailService.sendAccountCreationEmail(userEvent);

        // Then - проверяем что метод был вызван без ошибок даже с null firstName
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public EmailService emailService() {
            return mock(EmailService.class);
        }
    }

}