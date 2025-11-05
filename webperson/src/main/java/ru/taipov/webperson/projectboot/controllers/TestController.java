package ru.taipov.webperson.projectboot.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final Random random = new Random();

    @GetMapping("/unreliable")
    public String unreliableEndpoint() {
        // 50% вероятность ошибки для тестирования Circuit Breaker
        if (random.nextBoolean()) {
            throw new RuntimeException("Случайная ошибка для тестирования Circuit Breaker");
        }
        return "Успешный ответ";
    }

    @GetMapping("/slow")
    public String slowEndpoint() throws InterruptedException {
        // Имитация медленного ответа
        Thread.sleep(3000);
        return "Медленный ответ";
    }
}