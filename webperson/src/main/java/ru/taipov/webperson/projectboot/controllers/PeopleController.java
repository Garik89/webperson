package ru.taipov.webperson.projectboot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.taipov.webperson.projectboot.dto.PersonDTO;
import ru.taipov.webperson.projectboot.model.Person;
import ru.taipov.webperson.projectboot.services.PeopleService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/people")
@Tag(name = "People API", description = "API для управления пользователями")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    public ResponseEntity<List<Person>> getAllPeople() {
        return ResponseEntity.ok(peopleService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по указанному идентификатору")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") int id) {
        Person person = peopleService.findOne(id);
        return person != null ? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя с указанными данными")
    public ResponseEntity<?> createPerson(@RequestBody @Valid PersonDTO personDTO,
                                          BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Ошибка валидации");
        }

        Person savedPerson = peopleService.save(convertToPerson(personDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя по ID")
    public ResponseEntity<?> updatePerson(@PathVariable("id") int id,
                                          @RequestBody @Valid PersonDTO personDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Ошибка валидации");
        }

        Person updatedPerson = peopleService.update(id, convertToPerson(personDTO));
        return updatedPerson != null ? ResponseEntity.ok(updatedPerson) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по указанному ID")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") int id) throws JsonProcessingException {
        boolean deleted = peopleService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Person convertToPerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setName(personDTO.getName());
        person.setAge(personDTO.getAge());
        person.setEmail(personDTO.getEmail());
        return person;
    }
}
