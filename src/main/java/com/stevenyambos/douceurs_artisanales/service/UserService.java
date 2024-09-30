package com.stevenyambos.douceurs_artisanales.service;

import com.stevenyambos.douceurs_artisanales.model.UserModel;
import com.stevenyambos.douceurs_artisanales.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Récupérer toutes les enseignes
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    // S'inscrire
    public UserModel registerUser(UserModel user) {
        user.setIsSuperuser(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Se connecter
    public UserModel authenticate(String email, String password) {
        UserModel user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public UserModel getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Mettre à jour le profile
    public UserModel updateUser(String id, UserModel user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            user.setUpdatedAt(new Date());
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("L'utilisateur n'a pas été trouvé.");
        }
    }

    // Supprimer le compte
    public void deleteAccount(String id) {
        userRepository.deleteById(id);
    }
}
