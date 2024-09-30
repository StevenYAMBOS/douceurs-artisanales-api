package com.stevenyambos.douceurs_artisanales.config;

import com.stevenyambos.douceurs_artisanales.service.JwtService;
import com.stevenyambos.douceurs_artisanales.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// WebSecurityConfig est la classe de configuration principale pour la sécurité dans Spring Boot. Elle configure les filtres de sécurité, définit les routes protégées et les mécanismes d'authentification utilisés dans l'application.

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public WebSecurityConfig(@Lazy UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        // Autoriser les ADMINs et OWNERS à créer des boulangeries
                        .requestMatchers("/bakery/create").hasAnyRole("OWNER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Actuellement il faut être connecté pour faire des requêtes sauf pour les route "/auth/register" et "/auth/login", la logique ci-dessous permet d'accéder à des routes sans être connecté
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/", "/bakery/get-all", "/bakery/{id}", "/bakery/get-bakeries-by-zip-code", "/bakery/get-bakeries-count-by-zip-code"); // Exemple, personnalisez si nécessaire
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userService::getUserByEmail)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
