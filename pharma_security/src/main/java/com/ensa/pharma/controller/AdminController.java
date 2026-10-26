package com.ensa.pharma.controller;

import com.ensa.pharma.model.Role;
import com.ensa.pharma.model.User;
import com.ensa.pharma.repository.RoleRepository;
import com.ensa.pharma.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

/** Espace d'administration : gestion des utilisateurs (ADMIN uniquement, voir SecurityConfig). */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** Liste des utilisateurs + formulaire de création. */
    @GetMapping
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin";
    }

    /** Crée un utilisateur avec un rôle. */
    @PostMapping("/users/save")
    public String saveUser(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String roleName,
                           RedirectAttributes redirect) {
        if (userRepository.findByUsername(username).isPresent()) {
            redirect.addFlashAttribute("error", "Ce nom d'utilisateur existe déjà.");
            return "redirect:/admin";
        }
        Role role = roleRepository.findByName(roleName).orElseThrow();
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password)); // toujours hacher le mot de passe
        u.setRoles(Set.of(role));
        userRepository.save(u);
        redirect.addFlashAttribute("success", "Utilisateur créé : " + username);
        return "redirect:/admin";
    }

    /** Supprime un utilisateur. */
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
}
