package ru.taipov.webperson.projectboot.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc

class PeopleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    void testCreatePersonApi() throws Exception {
        // Генерируем уникальный email с UUID
        String uniqueEmail = "testuser_" + java.util.UUID.randomUUID() + "@example.com";
        System.out.println("Testing with email: " + uniqueEmail);

        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Иванввв\",\"age\":30,\"email\":\"" + uniqueEmail + "\"}"))
                .andDo(result -> {
                    // Детальный вывод всей информации об ошибке
                    System.out.println("=== RESPONSE STATUS: " + result.getResponse().getStatus());
                    System.out.println("=== RESPONSE CONTENT: " + result.getResponse().getContentAsString());
                    if (result.getResolvedException() != null) {
                        System.out.println("=== EXCEPTION: " + result.getResolvedException().getMessage());
                        result.getResolvedException().printStackTrace();
                    }
                })
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Иванввв"));
    }
}