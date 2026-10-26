package com.ensa.pharma.repository;

import com.ensa.pharma.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

/** Accès aux médicaments (CRUD complet fourni par JpaRepository). */
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
}
