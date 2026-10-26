package com.ensa.pharma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration centrale de la sécurité :
 *  - qui a le droit d'accéder à quelle URL (autorisation)
 *  - comment se connecter / se déconnecter (authentification)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Encodeur de mots de passe BCrypt.
     * Utilisé à la création des utilisateurs (hash) et au login (comparaison).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Chaîne de filtres de sécurité.
     * ATTENTION : l'ordre des règles compte, la première qui correspond gagne.
     * On met donc les règles les plus spécifiques en premier.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Ressources publiques : page de login, CSS, page d'erreur d'accès
                .requestMatchers("/login", "/css/**", "/access-denied").permitAll()

                // Espace administration : ADMIN uniquement
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Ajout / suppression de médicaments : ADMIN uniquement
                .requestMatchers("/medicaments/save", "/medicaments/delete/**").hasRole("ADMIN")

                // Modification du stock : ADMIN et PHARMACIEN
                .requestMatchers("/medicaments/stock/**").hasAnyRole("ADMIN", "PHARMACIEN")

                // Consultation des médicaments : tous sauf CAISSIER
                .requestMatchers("/medicaments").hasAnyRole("ADMIN", "PHARMACIEN", "CLIENT")

                // Ventes : ADMIN, PHARMACIEN et CAISSIER
                .requestMatchers("/ventes/**").hasAnyRole("ADMIN", "PHARMACIEN", "CAISSIER")

                // Toute autre URL (ex: /home) : il suffit d'être connecté
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")              // notre page login.html personnalisée
                .defaultSuccessUrl("/home", true) // redirection après succès
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")             // doit être appelé en POST (protection CSRF)
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Si l'utilisateur est connecté mais n'a pas le bon rôle -> page 403 personnalisée
            .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"));

        return http.build();
    }
}
