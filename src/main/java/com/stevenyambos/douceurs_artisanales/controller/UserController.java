package com.stevenyambos.douceurs_artisanales.controller;

import com.stevenyambos.douceurs_artisanales.model.UserModel;
import com.stevenyambos.douceurs_artisanales.repository.UserRepository;
import com.stevenyambos.douceurs_artisanales.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // Récupérer les informations de l'utilisateur connecté
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'USER')")
    @GetMapping("/profile")
    public ResponseEntity<UserModel> getUser(@RequestParam String email) {
        UserModel user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Mettre le profile
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'USER')")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserModel> updateProfile(@PathVariable("id") String id, @RequestBody UserModel user) {
        UserModel updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Supprimer le compte
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable("id") String id) {
        try {
            // Vérifier si l'utilisateur existe avant de le supprimer
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le compte avec l'ID " + id + " n'existe pas.");
            }

            // Supprimer le compte
            boolean isDeleted = userService.deleteAccount(id);

            if (!isDeleted) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Échec de la suppression du compte.");
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Compte supprimé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de la suppression du compte.");
        }
    }
}
