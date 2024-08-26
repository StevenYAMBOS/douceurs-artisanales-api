package com.stevenyambos.douceurs_artisanales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.stevenyambos.douceurs_artisanales.repository")
@SpringBootApplication
public class DouceursArtisanalesApplication {
	public static void main(String[] args) {
		SpringApplication.run(DouceursArtisanalesApplication.class, args);
	}
}
