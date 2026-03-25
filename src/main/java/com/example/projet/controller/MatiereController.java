package com.example.projet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.projet.entity.Matiere;
import com.example.projet.repository.MatiereRepository;

@RestController
@RequestMapping("/api/matieres")
@CrossOrigin
public class MatiereController {

    @Autowired
    private MatiereRepository matiereRepository;

    // API pour récupérer toutes les matières
    @GetMapping
    public List<Matiere> getAllMatieres() {
        return matiereRepository.findAll();
    }

}