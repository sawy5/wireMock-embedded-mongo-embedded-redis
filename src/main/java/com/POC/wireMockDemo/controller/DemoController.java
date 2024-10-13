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









    //    @GetMapping
//    public ResponseEntity<List<CatFact>> getFacts() {
//        return new ResponseEntity<>(demoService.getCatFacts(), HttpStatus.OK);
//    }

//    @GetMapping
//    public ResponseEntity<List<CatFact>> getFacts() {
//        try {
//            List<CatFact> facts = demoService.getCatFacts();
//            return new ResponseEntity<>(facts, HttpStatus.OK);
//        } catch (HttpClientErrorException.NotFound e) {
//            // Handle 404 Not Found
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Collections.singletonList(new CatFact()));
//        } catch (RuntimeException e) {
//            // Log and handle other exceptions
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null);
//        }
//    }
}
