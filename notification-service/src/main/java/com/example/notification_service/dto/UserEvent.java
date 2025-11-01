package com.example.notification_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // Аннотация Lombok, генерирует геттеры, сеттеры, toString и т.д.
public class UserEvent {
    private String operation; // "CREATE" или "DELETE"
    private String email;

}