package com.example.projet.repository;
import com.example.projet.entity.TypeSupport; 
import com.example.projet.entity.SupportCours;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupportCoursRepository extends JpaRepository<SupportCours, Long> {

    List<SupportCours> findByEnseignantId(Long enseignantId);
    // récupérer supports par matière
    List<SupportCours> findByMatiereId(Long matiereId);

    // récupérer supports par matière et type
    List<SupportCours> findByMatiereIdAndType(Long matiereId, TypeSupport type);
}