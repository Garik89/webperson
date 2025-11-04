package ru.taipov.webperson.projectboot.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.taipov.webperson.projectboot.dto.PersonDTO;
import ru.taipov.webperson.projectboot.model.Person;
import ru.taipov.webperson.projectboot.services.PeopleService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/people")
@Tag(name = "People API", description = "API для управления пользователями с HATEOAS")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей с навигационными ссылками")
    public ResponseEntity<CollectionModel<PersonDTO>> getAllPeople() {
        try {
            List<PersonDTO> people = peopleService.findAll().stream()
                    .map(this::convertToDTO)
                    .map(this::addLinksToPerson) // Добавляем ссылки к каждому пользователю
                    .collect(Collectors.toList());

            CollectionModel<PersonDTO> collectionModel = CollectionModel.of(people);

            // Добавляем глобальные ссылки
            collectionModel.add(linkTo(methodOn(PeopleController.class).getAllPeople()).withSelfRel());
            collectionModel.add(linkTo(methodOn(PeopleController.class).createPerson(null, null))
                    .withRel("create-person"));

            return ResponseEntity.ok(collectionModel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя с навигационными ссылками")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable("id") int id) {
        try {
            Person person = peopleService.findOne(id);
            if (person == null) {
                return ResponseEntity.notFound().build();
            }

            PersonDTO personDTO = addLinksToPerson(convertToDTO(person));
            return ResponseEntity.ok(personDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя и возвращает его с ссылками")
    public ResponseEntity<?> createPerson(@RequestBody @Valid PersonDTO personDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Ошибка валидации: " + bindingResult.getAllErrors());
        }

        try {
            Person person = convertToPerson(personDTO);
            Person savedPerson = peopleService.save(person);
            PersonDTO savedPersonDTO = addLinksToPerson(convertToDTO(savedPerson));

            return ResponseEntity
                    .created(savedPersonDTO.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(savedPersonDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет данные пользователя и возвращает его с обновленными ссылками")
    public ResponseEntity<?> updatePerson(@PathVariable("id") int id,
                                          @RequestBody @Valid PersonDTO personDTO,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Ошибка валидации: " + bindingResult.getAllErrors());
        }

        try {
            Person person = convertToPerson(personDTO);
            Person updatedPerson = peopleService.update(id, person);

            if (updatedPerson == null) {
                return ResponseEntity.notFound().build();
            }

            PersonDTO updatedPersonDTO = addLinksToPerson(convertToDTO(updatedPerson));
            return ResponseEntity.ok(updatedPersonDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя и возвращает ссылки для навигации")
    public ResponseEntity<?> deletePerson(@PathVariable("id") int id) {
        try {
            boolean deleted = peopleService.delete(id);
            if (!deleted) {
                return ResponseEntity.notFound().build();
            }

            // После удаления возвращаем ссылки для навигации
            CollectionModel<?> response = (CollectionModel<?>) CollectionModel.of(
                    java.util.Map.of("message", "Пользователь успешно удален")
            );
            response.add(linkTo(methodOn(PeopleController.class).getAllPeople()).withRel("all-people"));
            response.add(linkTo(methodOn(PeopleController.class).createPerson(null, null)).withRel("create-person"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }

    /**
     * Добавляет HATEOAS ссылки к DTO
     */
    private PersonDTO addLinksToPerson(PersonDTO personDTO) {
        if (personDTO.getId() != null) {
            personDTO.add(linkTo(methodOn(PeopleController.class).getPersonById(personDTO.getId())).withSelfRel());
            personDTO.add(linkTo(methodOn(PeopleController.class).updatePerson(personDTO.getId(), personDTO, null)).withRel("update"));
            personDTO.add(linkTo(methodOn(PeopleController.class).deletePerson(personDTO.getId())).withRel("delete"));
            personDTO.add(linkTo(methodOn(PeopleController.class).getAllPeople()).withRel("all-people"));
        }
        return personDTO;
    }

    /**
     * Конвертирует Person в PersonDTO
     */
    private PersonDTO convertToDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setAge(person.getAge());
        dto.setEmail(person.getEmail());
        return dto;
    }

    /**
     * Конвертирует PersonDTO в Person
     */
    private Person convertToPerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setName(personDTO.getName());
        person.setAge(personDTO.getAge());
        person.setEmail(personDTO.getEmail());
        return person;
    }
}