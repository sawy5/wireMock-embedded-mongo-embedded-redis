package com.POC.wireMockDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WireMockDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WireMockDemoApplication.class, args);
	}

}
