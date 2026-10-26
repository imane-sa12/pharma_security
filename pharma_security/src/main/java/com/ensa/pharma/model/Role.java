package com.ensa.pharma.model;

import jakarta.persistence.*;

/**
 * Un rôle : ADMIN, PHARMACIEN, CAISSIER ou CLIENT.
 * On stocke le nom SANS le préfixe "ROLE_" ; le préfixe est ajouté
 * dans CustomUserDetailsService (Spring Security l'exige pour hasRole()).
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
