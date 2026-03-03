package com.example.projet.repository;
import com.example.projet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.projet.entity.R;
import java.util.List;   

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndPassword(String email, String password);
    List<User> findByRole(R role);
    Optional<User> findByMatiereId(Long matiereId);

}

