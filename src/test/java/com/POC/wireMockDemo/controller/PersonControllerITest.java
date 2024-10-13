package com.POC.wireMockDemo.controller;

import com.POC.wireMockDemo.config.EmbeddedRedisConfig;
import com.POC.wireMockDemo.domain.Person;
import com.POC.wireMockDemo.repository.PersonRepository;
import com.POC.wireMockDemo.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
@Import(EmbeddedRedisConfig.class)
class PersonControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;
    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
    }

    @Test
    void testFindAllPeople() {
        Person person1 = Person.builder().name("adel").age(40).gender("male").build();
        Person person2 = Person.builder().name("nour").age(33).gender("female").build();

        personRepository.saveAll(List.of(person1, person2));

        ResponseEntity<List> response = restTemplate.getForEntity("/api/v1/person", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void testFindPersonById() {
        Person savedPerson = personRepository.save(Person.builder()
                .id("9")
                .name("george")
                .age(35)
                .gender("male")
                .build());

        ResponseEntity<Person> response = restTemplate
                .getForEntity("/api/v1/person/" + savedPerson.getId(), Person.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(savedPerson.getName());
    }

    @Test
    void testCreatePerson() {
        Person person = Person.builder()
                .name("sawy")
                .age(50)
                .gender("male")
                .build();

        ResponseEntity<Person> response = restTemplate.postForEntity("/api/v1/person", person, Person.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("sawy");
    }

    @Test
    void testDeletePerson() {
        Person savedPerson = personRepository.save(Person.builder()
                .id("9")
                .name("george")
                .age(35)
                .gender("male")
                .build());

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/v1/person/" + savedPerson.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Person> getResponse = restTemplate
                .getForEntity("/api/v1/person/" + savedPerson.getId(), Person.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testSaveAndFindPersonWithCaching() {
        Person person = Person.builder()
                .name("adel")
                .age(33)
                .gender("male")
                .build();

        Person savedPerson = personService.save(person);
        personRepository.deleteAll();

        // should be from cache
        Person foundPerson = personService.findById(savedPerson.getId());

        assertThat(foundPerson).isNotNull();
        assertThat(foundPerson.getName()).isEqualTo(savedPerson.getName());
        assertThat(cacheManager.getCache("person")).isNotNull();

        personService.deleteById(savedPerson.getId());

        assertThat(cacheManager.getCache("person").get(savedPerson.getId())).isNull();
    }
}