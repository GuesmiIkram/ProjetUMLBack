package com.example.projet.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "support_cours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportCours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeSupport type; // COURS, TP, TD

    @Column(nullable = false)
    private String fichierUrl;

    @ManyToOne
    @JoinColumn(name = "enseignant_id", nullable = false)
    private User enseignant;

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;
}