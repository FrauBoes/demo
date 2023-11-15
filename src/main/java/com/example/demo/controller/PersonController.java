package com.example.demo.controller;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/demo/persons")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping()
    public Iterable<Person> getAll() {
        return personRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> get(@PathVariable(value = "id") long id) {
        Optional<Person> person = personRepository.findById(id);

        return person.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public Person create(@Valid @RequestBody Person person) {
        return personRepository.save(person);
    }

    @PutMapping("/{id}")
    public Person update(@Valid @RequestBody Person newPerson, @PathVariable(value = "id") long id) {
        return personRepository.findById(id)
                .map(person -> {
                    person.setFirstName(newPerson.getFirstName());
                    person.setLastName(newPerson.getLastName());
                    person.setBirthDate(newPerson.getBirthDate());
                    return personRepository.save(person);
                })
                .orElseGet(() -> personRepository.save(newPerson));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") long id) {
        personRepository.deleteById(id);
    }
}
