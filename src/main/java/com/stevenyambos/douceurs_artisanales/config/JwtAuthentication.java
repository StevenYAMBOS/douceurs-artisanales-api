package com.stevenyambos.douceurs_artisanales.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

// Gère la génération et la validation des JWT.

public class JwtAuthentication extends UsernamePasswordAuthenticationToken {

    public JwtAuthentication(String principal) {
        super(principal, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
