package com.POC.wireMockDemo.controller;

import com.POC.wireMockDemo.domain.CatFact;
import com.POC.wireMockDemo.service.DemoService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facts")
@RequiredArgsConstructor
public class DemoController {

    private final DemoService demoService;

    @GetMapping
    public ResponseEntity<?> getFacts() {
        return demoService.getCatFacts();
    }


    @PostMapping
    public ResponseEntity<?> addFact(@RequestBody CatFact catFact) {
        return demoService.addCatFact(catFact);
    }


}
