package com.POC.wireMockDemo.service;

import com.POC.wireMockDemo.domain.Person;
import com.POC.wireMockDemo.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Cacheable(value = "person", key = "#id")
    public Person findById(String id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    @CachePut(value = "person", key = "#person.id")
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @CacheEvict(value = "person", key = "#id")
    public void deleteById(String id) {
        personRepository.deleteById(id);
    }
}
