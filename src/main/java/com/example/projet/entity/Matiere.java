package com.example.projet.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "matiere")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @OneToOne(mappedBy = "matiere")
    private User enseignant; // chaque matière a un seul prof

    @OneToMany(mappedBy = "matiere")
    private List<SupportCours> supports;
}