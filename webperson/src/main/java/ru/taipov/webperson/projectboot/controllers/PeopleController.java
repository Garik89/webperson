package ru.taipov.webperson.projectboot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.taipov.webperson.projectboot.dto.PersonDTO;
import ru.taipov.webperson.projectboot.model.Person;
import ru.taipov.webperson.projectboot.services.PeopleService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Igor Taipov
 */
@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }


    @PostMapping()
    public String create( /*@ModelAttribute("person")*/ @RequestBody @Valid PersonDTO personDTO,
                         BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.save(convertToPerson(personDTO));
        return "redirect:/people";
    }



    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) throws JsonProcessingException {
        peopleService.delete(id);
        return "redirect:/people";
    }


    private Person convertToPerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setAge(personDTO.getAge());
        person.setName(personDTO.getName());
        person.setEmail(personDTO.getEmail());



        return person;
    }


}
