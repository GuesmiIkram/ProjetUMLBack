
package com.example.projet.controller;
import org.springframework.web.multipart.MultipartFile;
import com.example.projet.entity.User;
import com.example.projet.entity.TypeSupport;
import com.example.projet.repository.UserRepository;
import com.example.projet.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import com.example.projet.entity.SupportCours;
import com.example.projet.repository.SupportCoursRepository;
import com.example.projet.repository.MatiereRepository;
import com.example.projet.entity.R;
import java.util.Map;
import java.util.Optional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/supports")

public class SupportCoursController {

    @Autowired
    private SupportCoursRepository supportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatiereRepository matiereRepository;
    @PostMapping("/add/{enseignantId}")
    public ResponseEntity<?> addSupport(
        @PathVariable Long enseignantId,
        @RequestParam("titre") String titre,
        @RequestParam("description") String description,
        @RequestParam("type") String type,
        @RequestParam("file") MultipartFile file) {

    Optional<User> enseignantOpt = userRepository.findById(enseignantId);

    if (enseignantOpt.isEmpty() || enseignantOpt.get().getRole() != R.ENSEIGNANT) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Seul un enseignant peut ajouter un support");
    }

    try {

        String uploadDir = "uploads/";
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        // nom fichier unique
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path path = Paths.get(uploadDir + fileName);

        Files.write(path, file.getBytes());

        SupportCours support = new SupportCours();
        support.setTitre(titre);
        support.setDescription(description);
        support.setType(TypeSupport.valueOf(type.toUpperCase()));

        // ⚠️ ON STOCKE SEULEMENT LE NOM
        support.setFichierUrl(fileName);

        support.setEnseignant(enseignantOpt.get());
        support.setMatiere(enseignantOpt.get().getMatiere());

        supportRepository.save(support);

        return ResponseEntity.ok("Support ajouté avec succès");

    } catch (Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de l'upload");
        }}


    @DeleteMapping("/delete/{supportId}/{enseignantId}")
public ResponseEntity<?> deleteSupport(
        @PathVariable Long supportId,
        @PathVariable Long enseignantId) {

    Optional<SupportCours> supportOpt = supportRepository.findById(supportId);

    if (supportOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Support non trouvé");
    }

    SupportCours support = supportOpt.get();

    // Vérifier que le support appartient à cet enseignant
    if (!support.getEnseignant().getId().equals(enseignantId)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Vous ne pouvez supprimer que vos propres supports");
    }

    try {

        // supprimer le fichier physique
        String uploadDir = "uploads/";
        Path filePath = Paths.get(uploadDir + support.getFichierUrl());

        Files.deleteIfExists(filePath);

        // supprimer de la base
        supportRepository.deleteById(supportId);

        return ResponseEntity.ok("Support supprimé avec succès");

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors de la suppression");
    }
}

@GetMapping("/enseignant/{enseignantId}")
public ResponseEntity<List<SupportCours>> getSupportsByEnseignant(@PathVariable Long enseignantId) {

    // Vérifier que l'enseignant existe
    Optional<User> enseignantOpt = userRepository.findById(enseignantId);
    if (enseignantOpt.isEmpty() || enseignantOpt.get().getRole() != R.ENSEIGNANT) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(null);
    }

    // Récupérer tous les supports liés à cet enseignant
    List<SupportCours> supports = supportRepository.findByEnseignantId(enseignantId);

    return ResponseEntity.ok(supports);
}
    // Supprimer un support
    /*@DeleteMapping("/delete/{id}/{enseignantId}")
    public ResponseEntity<?> deleteSupport(@PathVariable Long id, @PathVariable Long enseignantId) {
        Optional<SupportCours> supportOpt = supportRepository.findById(id);

        if(supportOpt.isPresent() && supportOpt.get().getEnseignant().getId().equals(enseignantId)) {
            supportRepository.deleteById(id);
            return ResponseEntity.ok("Support supprimé");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous ne pouvez supprimer que vos propres supports");
        }
    }

    // Lister les supports de l’enseignant
    @GetMapping("/enseignant/{enseignantId}")
    public ResponseEntity<List<SupportCours>> getSupportsByEnseignant(@PathVariable Long enseignantId) {
        List<SupportCours> supports = supportRepository.findByEnseignantId(enseignantId);
        return ResponseEntity.ok(supports);
    }*/
}