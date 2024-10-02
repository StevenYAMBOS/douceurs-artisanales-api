package com.stevenyambos.douceurs_artisanales.controller;

import com.stevenyambos.douceurs_artisanales.model.UserModel;
import com.stevenyambos.douceurs_artisanales.repository.UserRepository;
import com.stevenyambos.douceurs_artisanales.service.UserService;
import com.stevenyambos.douceurs_artisanales.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
//import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

//    S'inscrire
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserModel user) {
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur: Cette adresse email est déjà utilisée !");
        }

        if (user.getIsOwner()) {
            user.setRole(UserModel.Role.OWNER);
        } else {
            user.setRole(UserModel.Role.USER);
        }

        UserModel createdUser = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

//    Se connecter
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        UserModel user = userService.authenticate(email, password);

        if (user != null) {
            String token = jwtService.generateToken(user);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);

            user.setLastLogin(new Date());
            System.out.println(response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Adresse email ou mot de passe incorrect");
        }
    }
}
