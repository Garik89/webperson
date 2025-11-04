package ru.taipov.webperson.projectboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.taipov.webperson.projectboot.model.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
   // Optional<Person> findByUsername(String username);
       Optional<Person> findByEmail(String email);


}
