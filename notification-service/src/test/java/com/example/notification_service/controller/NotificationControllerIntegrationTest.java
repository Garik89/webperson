package com.example.notification_service.controller;

import com.example.notification_service.dto.EmailRequest;
import com.example.notification_service.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoSpyBean
    private EmailService emailService;

    @Test
    void testSendEmail_CreateOperation() throws Exception {
        // Given
        EmailRequest request = new EmailRequest();
        request.setOperation("CREATE");
        request.setEmail("test@example.com");


        // Mock email service
        doNothing().when(emailService).sendAccountCreationEmail(any());

        // When & Then
        mockMvc.perform(post("/api/notifications/send-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully to: test@example.com"));
    }

    @Test
    void testSendEmail_DeleteOperation() throws Exception {
        // Given
        EmailRequest request = new EmailRequest();
        request.setOperation("DELETE");
        request.setEmail("test@example.com");


        // Mock email service
        doNothing().when(emailService).sendAccountDeletionEmail(any());

        // When & Then
        mockMvc.perform(post("/api/notifications/send-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully to: test@example.com"));
    }

    @Test
    void testSendEmail_InvalidOperation() throws Exception {
        // Given
        EmailRequest request = new EmailRequest();
        request.setOperation("INVALID");
        request.setEmail("test@example.com");


        // When & Then
        mockMvc.perform(post("/api/notifications/send-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unknown operation: INVALID"));
    }

    @Test
    void testHealthEndpoint() throws Exception {
        // Исправьте на GET запрос
        mockMvc.perform(get("/api/notifications/health"))
                .andExpect(status().isOk());
    }

    @Test
    void testSendEmail_MissingFields() throws Exception {
        // Given - request with missing email
        String invalidJson = "{\"operation\":\"CREATE\",\"firstName\":\"John\"}";

        // When & Then
        mockMvc.perform(post("/api/notifications/send-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}