package com.stevenyambos.douceurs_artisanales.controller;

import com.stevenyambos.douceurs_artisanales.model.UserModel;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("id") String id) {
        userService.deleteAccount(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
