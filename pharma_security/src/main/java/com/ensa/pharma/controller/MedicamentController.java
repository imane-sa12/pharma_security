package com.ensa.pharma.controller;

import com.ensa.pharma.model.Medicament;
import com.ensa.pharma.repository.MedicamentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Gestion des médicaments.
 * Les droits d'accès sont définis dans SecurityConfig :
 *  - GET /medicaments          : ADMIN, PHARMACIEN, CLIENT (lecture)
 *  - /medicaments/stock/{id}   : ADMIN, PHARMACIEN (modifier le stock)
 *  - save / delete             : ADMIN
 */
@Controller
@RequestMapping("/medicaments")
public class MedicamentController {

    private final MedicamentRepository medicamentRepository;

    public MedicamentController(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    /** Liste des médicaments. */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("medicaments", medicamentRepository.findAll());
        return "medicaments";
    }

    /** Ajouter un médicament (ADMIN). */
    @PostMapping("/save")
    public String save(@RequestParam String nom,
                       @RequestParam(required = false) String description,
                       @RequestParam double prix,
                       @RequestParam int stock) {
        medicamentRepository.save(new Medicament(nom, description, prix, stock));
        return "redirect:/medicaments";
    }

    /** Modifier le stock d'un médicament (ADMIN, PHARMACIEN). */
    @PostMapping("/stock/{id}")
    public String updateStock(@PathVariable Long id, @RequestParam int stock) {
        medicamentRepository.findById(id).ifPresent(m -> {
            m.setStock(Math.max(stock, 0)); // pas de stock négatif
            medicamentRepository.save(m);
        });
        return "redirect:/medicaments";
    }

    /** Supprimer un médicament (ADMIN). */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        medicamentRepository.deleteById(id);
        return "redirect:/medicaments";
    }
}
