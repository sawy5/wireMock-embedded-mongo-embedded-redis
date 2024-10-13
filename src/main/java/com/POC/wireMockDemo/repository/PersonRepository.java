package com.POC.wireMockDemo.repository;

import com.POC.wireMockDemo.domain.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {

}
