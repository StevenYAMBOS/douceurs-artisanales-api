package com.stevenyambos.douceurs_artisanales.config;

import com.stevenyambos.douceurs_artisanales.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JwtAuthenticationFilter est un filtre de servlet (HTTP Filter) qui intercepte toutes les requêtes HTTP avant qu'elles n'atteignent le contrôleur. Il vérifie la présence d'un JWT dans l'en-tête de la requête (Authorization) et valide ce token.

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        String user = jwtService.validateToken(token);

        if (user != null) {
            SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(user));
        }

        filterChain.doFilter(request, response);
    }
}
