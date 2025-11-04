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

//import javax.validation.Valid;
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

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    @Transactional
    public Person save(Person person) {
        person.setCreated_at(new Date());
        return peopleRepository.save(person);
    }

    @Transactional
    public Person update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        return peopleRepository.save(updatedPerson);
    }

    @Transactional
    public Boolean delete(int id) {
        if (peopleRepository.existsById(id)) {
            peopleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}