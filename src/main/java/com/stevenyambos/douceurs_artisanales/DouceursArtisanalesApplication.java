package com.stevenyambos.douceurs_artisanales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class DouceursArtisanalesApplication {
	public static void main(String[] args) {
		SpringApplication.run(DouceursArtisanalesApplication.class, args);

	}
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "Steven") String name) {
		return String.format("Wesh ma gueule, c'est mon gars %s ", name);
	}


}
