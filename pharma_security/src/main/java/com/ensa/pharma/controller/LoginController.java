package com.ensa.pharma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Pages générales : login, accueil, accès refusé. */
@Controller
public class LoginController {

    /** Affiche le formulaire de connexion (le traitement du POST /login est fait par Spring Security). */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /** Page d'accueil après connexion (le menu dépend du rôle, voir home.html). */
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    /** Page affichée quand l'utilisateur n'a pas le droit (erreur 403). */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
