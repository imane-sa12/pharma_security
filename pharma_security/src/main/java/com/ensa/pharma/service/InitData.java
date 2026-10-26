package com.ensa.pharma.service;

import com.ensa.pharma.model.Medicament;
import com.ensa.pharma.model.Role;
import com.ensa.pharma.model.User;
import com.ensa.pharma.repository.MedicamentRepository;
import com.ensa.pharma.repository.RoleRepository;
import com.ensa.pharma.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Initialise la base au démarrage : rôles, utilisateurs de test, médicaments.
 * CommandLineRunner.run() est exécuté une fois, juste après le démarrage.
 * Les vérifications évitent les doublons aux redémarrages.
 */
@Component
public class InitData implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final MedicamentRepository medicamentRepository;
    private final PasswordEncoder passwordEncoder;

    public InitData(RoleRepository roleRepository, UserRepository userRepository,
                    MedicamentRepository medicamentRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.medicamentRepository = medicamentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // --- 1. Rôles ---
        for (String name : new String[]{"ADMIN", "PHARMACIEN", "CAISSIER", "CLIENT"}) {
            if (roleRepository.findByName(name).isEmpty()) {
                roleRepository.save(new Role(name));
            }
        }

        // --- 2. Utilisateurs de test (mot de passe haché avec BCrypt) ---
        createUserIfAbsent("admin", "admin123", "ADMIN");
        createUserIfAbsent("pharma", "pharma123", "PHARMACIEN");
        createUserIfAbsent("caisse", "caisse123", "CAISSIER");
        createUserIfAbsent("client", "client123", "CLIENT"); // utilisateur bonus pour tester CLIENT

        // --- 3. Médicaments d'exemple ---
        if (medicamentRepository.count() == 0) {
            medicamentRepository.save(new Medicament("Doliprane 1000mg", "Antidouleur / fièvre", 18.50, 100));
            medicamentRepository.save(new Medicament("Amoxicilline 500mg", "Antibiotique", 42.00, 50));
            medicamentRepository.save(new Medicament("Ibuprofène 400mg", "Anti-inflammatoire", 25.00, 75));
            medicamentRepository.save(new Medicament("Vitamine C 1g", "Complément alimentaire", 30.00, 120));
        }
    }

    /** Crée l'utilisateur seulement s'il n'existe pas encore. */
    private void createUserIfAbsent(String username, String rawPassword, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) return;

        Role role = roleRepository.findByName(roleName).orElseThrow();
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(rawPassword)); // hachage BCrypt
        u.setEnabled(true);
        u.setRoles(Set.of(role));
        userRepository.save(u);
    }
}
