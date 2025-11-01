package com.example.notification_service.kafka;

import com.example.notification_service.dto.UserEvent;
import com.example.notification_service.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventsConsumer {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    // ИЗМЕНИТЕ ПАРАМЕТР МЕТОДА НА String
    @KafkaListener(topics = "my_topic")
    public void handleUserEvent(UserEvent event) {
        log.info("Received raw message: {}", event.getOperation());

        try {
            // Парсим JSON вручную
            UserEvent userEvent = objectMapper.readValue(event.getOperation(), UserEvent.class);
            log.info("Parsed UserEvent: {}", userEvent);

            switch (userEvent.getOperation().toUpperCase()) {
                case "CREATE":
                    emailService.sendAccountCreationEmail(userEvent);
                    break;
                case "DELETE":
                    emailService.sendAccountDeletionEmail(userEvent);
                    break;
                default:
                    log.warn("Unknown operation received: {}", userEvent.getOperation());
            }
            log.info("Successfully processed event for user: {}", userEvent.getEmail());
        } catch (Exception e) {
            log.error("Failed to process message: {}", event.getOperation(), e);
        }
    }
}