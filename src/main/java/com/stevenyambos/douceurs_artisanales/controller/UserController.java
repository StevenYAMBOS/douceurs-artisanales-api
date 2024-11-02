package com.stevenyambos.douceurs_artisanales.controller;

import com.stevenyambos.douceurs_artisanales.dto.AddToFavoritesRequest;
import com.stevenyambos.douceurs_artisanales.model.BakeryModel;
import com.stevenyambos.douceurs_artisanales.model.UserModel;
import com.stevenyambos.douceurs_artisanales.repository.UserRepository;
import com.stevenyambos.douceurs_artisanales.service.BakeryService;
import com.stevenyambos.douceurs_artisanales.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BakeryService bakeryService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, BakeryService bakeryService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.bakeryService = bakeryService;
    }

    // Récuprer tous les utilisateurs
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
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


    // Ajouter une boulangerie aux favoris (
    @PutMapping("/bakery/favorites")
    public ResponseEntity<?> addToFavorites(@RequestBody AddToFavoritesRequest request) {
        try {
            // Récupérer l'utilisateur par ID
            UserModel user = userService.getUserByEmail(request.getUserEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
            }

            // Initialiser la liste `Likes` si elle est null
            if (user.getLikes() == null) {
                user.setLikes(new ArrayList<>());
            }

            // Récupérer la boulangerie par ID
            BakeryModel bakery = bakeryService.getBakeryById(request.getBakeryId());
            if (bakery == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Boulangerie non trouvée.");
            }

            // Vérifier si la boulangerie est déjà dans les favoris de l'utilisateur
            Optional<BakeryModel> existingInFavorite = user.getLikes().stream()
                    .filter(b -> b.getId().equals(request.getBakeryId()))
                    .findFirst();

            if (existingInFavorite.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La boulangerie est déjà dans les favoris.");
            } else {
                // Ajouter la boulangerie aux favoris de l'utilisateur
                user.getLikes().add(bakery);
                userService.updateUser(user.getId(), user); // Mettre à jour l'utilisateur
                return ResponseEntity.ok("Boulangerie ajoutée aux favoris.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Retirer une boulangerie des favoris
    @DeleteMapping("/bakery/favorites")
    public ResponseEntity<?> removeFromFavorites(@RequestBody AddToFavoritesRequest request) {
        try {
            // Récupérer l'utilisateur par email
            UserModel user = userService.getUserByEmail(request.getUserEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
            }

            // Trouver la boulangerie par ID dans la liste `Likes`
            Optional<BakeryModel> bakeryToRemove = user.getLikes().stream()
                    .filter(bakery -> bakery.getId().equals(request.getBakeryId()))
                    .findFirst();

            if (bakeryToRemove.isPresent()) {
                user.getLikes().remove(bakeryToRemove.get());
                userService.updateUser(user.getId(), user); // Mettre à jour l'utilisateur

                return ResponseEntity.ok("Boulangerie supprimée des favoris.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Boulangerie non trouvée dans les favoris de l'utilisateur.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression des favoris.");
        }
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
