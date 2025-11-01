package com.example.notification_service.controller;

import com.example.notification_service.dto.EmailRequest;
import com.example.notification_service.dto.UserEvent;
import com.example.notification_service.services.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest request) {
        if (!isValidOperation(request.getOperation())) {
            return ResponseEntity.badRequest().body("Unknown operation: " + request.getOperation());
        }

        try {
            // Создаем UserEvent из EmailRequest
            UserEvent userEvent = new UserEvent(request.getOperation(), request.getEmail());

            switch (request.getOperation().toUpperCase()) {
                case "CREATE":
                    emailService.sendAccountCreationEmail(userEvent);
                    break;
                case "DELETE":
                    emailService.sendAccountDeletionEmail(userEvent);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Unknown operation: " + request.getOperation());
            }
            return ResponseEntity.ok("Email sent successfully to: " + request.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

    private boolean isValidOperation(String operation) {
        return operation != null &&
                (operation.equalsIgnoreCase("CREATE") || operation.equalsIgnoreCase("DELETE"));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is healthy");
    }
}