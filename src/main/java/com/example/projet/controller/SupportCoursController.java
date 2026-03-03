
package com.example.projet.controller;

import com.example.projet.entity.User;
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

    if (enseignantOpt.isPresent() &&
        enseignantOpt.get().getRole() == R.ENSEIGNANT) {

        try {
            // 📁 Créer dossier uploads s'il n'existe pas
            String uploadDir = "uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdir();
            }

            // 📄 Nom fichier
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            // 💾 Sauvegarde fichier dans dossier
            Files.write(filePath, file.getBytes());

            // 🗄️ Sauvegarde en base
            SupportCours support = new SupportCours();
            support.setTitre(titre);
            support.setDescription(description);
            support.setType(TypeSupport.valueOf(type));
            support.setFichierUrl(uploadDir + fileName);
            support.setEnseignant(enseignantOpt.get());
            support.setMatiere(enseignantOpt.get().getMatiere());

            supportRepository.save(support);

            return ResponseEntity.ok("Support ajouté avec succès");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du téléchargement du fichier");
        }

    } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Seul un enseignant peut ajouter un support");
    }
}
    // Supprimer un support
    @DeleteMapping("/delete/{id}/{enseignantId}")
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
    }
}