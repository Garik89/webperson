package ru.taipov.webperson.projectboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.taipov.webperson.projectboot.controllers.PeopleController;
import ru.taipov.webperson.projectboot.dto.PersonDTO;
import ru.taipov.webperson.projectboot.services.PeopleService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PeopleController.class)
public class PeopleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PeopleService peopleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePersonApi() throws Exception {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Ива");
        personDTO.setAge(30);
        personDTO.setEmail("ivn@examle.com");

        String json = objectMapper.writeValueAsString(personDTO);

        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is3xxRedirection()); // Ваша логика редиректа
    }
}