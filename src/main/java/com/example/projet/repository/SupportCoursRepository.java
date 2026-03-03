package com.example.projet.repository;

import com.example.projet.entity.SupportCours;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupportCoursRepository extends JpaRepository<SupportCours, Long> {

    List<SupportCours> findByEnseignantId(Long enseignantId);
}