package com.ensa.pharma.repository;

import com.ensa.pharma.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/** Accès aux utilisateurs. Spring Data génère l'implémentation automatiquement. */
public interface UserRepository extends JpaRepository<User, Long> {
    // Requête déduite du nom de la méthode : SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);
}
