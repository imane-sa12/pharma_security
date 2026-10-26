package com.ensa.pharma.controller;

import com.ensa.pharma.model.Medicament;
import com.ensa.pharma.repository.MedicamentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Enregistrement des ventes (ADMIN, PHARMACIEN, CAISSIER).
 * Une vente diminue simplement le stock du médicament vendu.
 */
@Controller
@RequestMapping("/ventes")
public class VenteController {

    private final MedicamentRepository medicamentRepository;

    public VenteController(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    /** Formulaire de vente. */
    @GetMapping
    public String form(Model model) {
        model.addAttribute("medicaments", medicamentRepository.findAll());
        return "ventes";
    }

    /** Enregistre la vente : vérifie le stock puis le décrémente. */
    @PostMapping("/vendre")
    @Transactional
    public String vendre(@RequestParam Long medicamentId,
                         @RequestParam int quantite,
                         RedirectAttributes redirect) {
        Medicament m = medicamentRepository.findById(medicamentId).orElse(null);

        if (m == null || quantite <= 0) {
            redirect.addFlashAttribute("error", "Données de vente invalides.");
        } else if (quantite > m.getStock()) {
            redirect.addFlashAttribute("error",
                    "Stock insuffisant pour " + m.getNom() + " (disponible : " + m.getStock() + ").");
        } else {
            m.setStock(m.getStock() - quantite);
            medicamentRepository.save(m);
            redirect.addFlashAttribute("success",
                    "Vente enregistrée : " + quantite + " x " + m.getNom()
                            + " = " + (quantite * m.getPrix()) + " DH");
        }
        return "redirect:/ventes";
    }
}
