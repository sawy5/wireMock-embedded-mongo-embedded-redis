package com.POC.wireMockDemo.service;

import com.POC.wireMockDemo.domain.CatFact;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final RestTemplate restTemplate;
    @Value("${demo.service.url}")
    private String url;

    public ResponseEntity<?> getCatFacts() {
        try {
            ResponseEntity<List<CatFact>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CatFact>>() {}
            );
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    public ResponseEntity<?> addCatFact(CatFact catFact) {
        try{
            ResponseEntity<CatFact> response = restTemplate.postForEntity(
                    url,
                    catFact,
                    CatFact.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

}
