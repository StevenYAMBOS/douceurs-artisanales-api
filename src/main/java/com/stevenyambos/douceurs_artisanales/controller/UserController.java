package com.stevenyambos.douceurs_artisanales.controller;

import com.stevenyambos.douceurs_artisanales.model.UserModel;
import com.stevenyambos.douceurs_artisanales.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Récupérer les informations de l'utilisateur connecté
    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserModel> getUser(@RequestParam String email) {
        UserModel user = userService.getUserByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Mettre le profile
    @PutMapping("/update/{id}")
    public ResponseEntity<UserModel> updateProfile(@PathVariable("id") String id, @RequestBody UserModel user) {
        UserModel updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Supprimer le compte
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("id") String id) {
        userService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
