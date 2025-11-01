
package com.example.notification_service.kafka;

import com.example.notification_service.dto.UserEvent;
import com.example.notification_service.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"my_topic"}, brokerProperties = {"listeners=PLAINTEXT://localhost:0", "port=0"})
class UserEventsConsumerIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate; // Используем Object вместо UserEvent

    @MockitoSpyBean
    private EmailService emailService;

    @Test
    void testKafkaMessageConsumption_CreateOperation() throws Exception {
        // Given
        UserEvent userEvent = new UserEvent("CREATE", "test@example.com");

        // When
        kafkaTemplate.send("my_topic", userEvent).get(5, TimeUnit.SECONDS);

        // Then
        verify(emailService, timeout(10000)).sendAccountCreationEmail(any(UserEvent.class));
    }

    @Test
    void testKafkaMessageConsumption_DeleteOperation() throws Exception {
        // Given
        UserEvent userEvent = new UserEvent("DELETE", "test@example.com");

        // When
        kafkaTemplate.send("my_topic", userEvent).get(5, TimeUnit.SECONDS);

        // Then
        verify(emailService, timeout(10000)).sendAccountDeletionEmail(any(UserEvent.class));
    }
}