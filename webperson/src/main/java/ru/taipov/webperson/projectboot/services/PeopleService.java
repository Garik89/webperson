package ru.taipov.webperson.projectboot.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.taipov.webperson.projectboot.model.Person;
import ru.taipov.webperson.projectboot.repositories.PeopleRepository;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author Igor Taipov
 */
@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository,KafkaTemplate<String, String> kafkaTemplate ) {
        this.peopleRepository = peopleRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public Person save(@Valid Person person) throws JsonProcessingException {

        otherconvertToPerson(person);
        peopleRepository.save(person);

        sendUserEvent(person.getEmail(), "create");
        return person;


    }

    @Transactional
    public Person update(int id, @Valid Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
        return  updatedPerson;
    }

    @Transactional
    public Boolean delete(int id) throws JsonProcessingException {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        peopleRepository.deleteById(id);

        if(foundPerson.orElse(null) != null)
        {
            sendUserEvent(foundPerson.orElse(null).getEmail(), "delete");
        }

        return foundPerson.orElse(null) != null;


    }

    private void otherconvertToPerson(Person person)
    {
        person.setCreated_at(new Date());

    }

    private void sendUserEvent(String email, String operation) throws JsonProcessingException {
        Map<String, String> message = new HashMap<>();
        message.put("email", email);
        message.put("operation", operation); // "create" / "delete"
        String jsonMessage = new ObjectMapper().writeValueAsString(message);
        kafkaTemplate.send(kafkaTopic, jsonMessage);
    }
}
