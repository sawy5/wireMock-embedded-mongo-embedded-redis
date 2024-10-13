package com.POC.wireMockDemo.controller;

import com.POC.wireMockDemo.domain.CatFact;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WireMockTest(httpPort = 8081)
class DemoControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private WireMockServer wireMockServer;
    SoftAssertions softly = new SoftAssertions();

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8081));
//        wireMockServer = new WireMockServer(8081);
        wireMockServer.start();
//        configureFor(wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testGetFacts_OK() throws IOException {
        String jsonBody = Files.readString(Paths.get("src/test/resources/response/facts_success.json"));

        wireMockServer.stubFor(get(urlEqualTo("/facts"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type","application/json")
                        .withBody(jsonBody)));

        ResponseEntity<List> response = restTemplate.getForEntity("/api/v1/facts", List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);

        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/facts")));
    }

    @Test
    void testGetFacts_BadRequest() throws IOException {
        String jsonBody = Files.readString(Paths.get("src/test/resources/response/facts_badRequest.json"));
        Map<String, String> requestBody1 = new HashMap<>();
        requestBody1.put("_id", "invalid");
        requestBody1.put("text", "invalid");

        wireMockServer.stubFor(post(urlEqualTo("/facts"))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(requestBody1)))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type","application/json")
                        .withBody(jsonBody)));

        ResponseEntity<Map> response = restTemplate.postForEntity("/api/v1/facts", requestBody1, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).containsEntry("message", "invalid input");
    }

    @Test
    void testGetFacts_NotFound() throws IOException {
        String jsonBody = Files.readString(Paths.get("src/test/resources/response/facts_notFound.json"));

        wireMockServer.stubFor(get(urlEqualTo("/facts"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonBody)));

        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/facts", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("message")).isEqualTo("Facts not found");

        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/facts")));
    }

    @Test
    void testGetFacts_InternalServerError() throws IOException {
        String jsonBody = Files.readString(Paths.get("src/test/resources/response/facts_internalServerError.json"));

        wireMockServer.stubFor(get(urlEqualTo("/facts"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonBody)));

        ResponseEntity<Map> response = restTemplate.getForEntity("/api/v1/facts", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("message")).isEqualTo("Internal server error occurred");

        wireMockServer.verify(1, getRequestedFor(urlEqualTo("/facts")));
    }

}