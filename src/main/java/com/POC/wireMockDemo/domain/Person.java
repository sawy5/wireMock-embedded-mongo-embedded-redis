package com.POC.wireMockDemo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "person")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {
    @Id
    private String id;
    private String name;
    private Integer age;
    private String gender;
}
