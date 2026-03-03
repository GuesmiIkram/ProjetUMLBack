package com.example.projet.controller;

import com.example.projet.entity.User;
import com.example.projet.repository.UserRepository;
import com.example.projet.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
/* 
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        Optional<User> user = userRepository.findByEmailAndPassword(
            request.getEmail(), 
            request.getPassword()
        );

        if (user.isPresent()) {
            return "Connexion réussie ! Bienvenue " + user.get().getPrenom();
        } else {
            return "Email ou mot de passe incorrect";
        }
    }*/

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<User> user = userRepository.findByEmailAndPassword(
        request.getEmail(),
        request.getPassword()
    );

    if (user.isPresent()) {
        return ResponseEntity.ok().body(Map.of(
            "message", "Connexion réussie",
            "role", user.get().getRole(),
            "prenom", user.get().getPrenom(),
            "id", user.get().getId()
        ));
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Email ou mot de passe incorrect"));
    }
}
}