package com.ensa.pharma.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Utilisateur de l'application.
 * La table s'appelle "users" car "user" est un mot réservé dans certaines bases.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // Mot de passe stocké CRYPTÉ (BCrypt), jamais en clair
    @Column(nullable = false)
    private String password;

    private boolean enabled = true;

    // Relation plusieurs-à-plusieurs : un utilisateur peut avoir plusieurs rôles.
    // EAGER : les rôles sont chargés immédiatement (nécessaire pour l'authentification).
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
