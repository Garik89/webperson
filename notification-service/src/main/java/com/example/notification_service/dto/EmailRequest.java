package com.example.notification_service.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String operation; // "CREATE" или "DELETE"
    private String email;

}