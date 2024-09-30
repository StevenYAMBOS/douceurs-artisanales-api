package com.stevenyambos.douceurs_artisanales.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Autorise tous les endpoints
                        .allowedOrigins("http://localhost:5173")  // Front-End autorisé
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // Méthodes HTTP autorisées
                        .allowedHeaders("*")  // Tous les en-têtes autorisés
                        .allowCredentials(true);  // Si tu utilises des cookies ou des authentifications basées sur session
            }
        };
    }
}

