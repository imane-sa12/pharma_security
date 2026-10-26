package com.ensa.pharma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application.
 * @SpringBootApplication = configuration automatique + scan des composants
 * du package com.ensa.pharma et de ses sous-packages.
 */
@SpringBootApplication
public class PharmaSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(PharmaSecurityApplication.class, args);
    }
}
