package com.POC.wireMockDemo.controller;

import com.POC.wireMockDemo.domain.Person;
import com.POC.wireMockDemo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/person")
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<Person>> findAllPeople(){
        List<Person> people = personService.findAll();
        return ResponseEntity.ok(people);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findPersonById(@PathVariable String id){
        try {
            return ResponseEntity.ok(personService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person){
        Person createdPerson = personService.save(person);
        return ResponseEntity.ok(createdPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id){
        personService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
