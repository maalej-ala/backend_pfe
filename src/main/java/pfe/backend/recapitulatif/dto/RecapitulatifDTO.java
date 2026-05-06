// package pfe.backend.recapitulatif.dto;

package pfe.backend.recapitulatif.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RecapitulatifDTO {

    // ── Identification ────────────────────────────────────────
    private String civilite;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;

    // ── Vérification identité (CIN) ───────────────────────────
    private String cin;
    private LocalDate dateExpiration;
    private boolean estClientAutreBanque;

    // ── Adresse ───────────────────────────────────────────────
    private String adresse;
    private String paysNom;
    private String gouvernorat;
    private String codePostal;

    // ── Situation personnelle ─────────────────────────────────
    private String nationalite;
    private String statutCivil;
    private int nbEnfants;

    // ── Situation professionnelle ─────────────────────────────
    private String categorieSocioPro;
    private String revenu;
    private String natureActivite;
    private String secteurActivite;
}