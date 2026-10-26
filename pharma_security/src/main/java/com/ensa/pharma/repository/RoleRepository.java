package com.ensa.pharma.repository;

import com.ensa.pharma.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/** Accès aux rôles. */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
