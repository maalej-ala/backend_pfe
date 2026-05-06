package pfe.backend.signature.dto;

import lombok.Data;

@Data
public class SignatureDTO {

    // ── Identité ──────────────────────────────────────────────────
    private String civilite;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String dateNaissance;       // format "yyyy-MM-dd" ou "dd/MM/yyyy"

    // ── CIN ───────────────────────────────────────────────────────
    private String cin;
    private String dateExpiration;      // format "yyyy-MM-dd" ou "dd/MM/yyyy"
    private boolean estClientAutreBanque;

    // ── Adresse ───────────────────────────────────────────────────
    private String adresse;
    private String paysNom;
    private String gouvernorat;
    private String codePostal;

    // ── Situation personnelle ─────────────────────────────────────
    private String nationalite;
    private String statutCivil;
    private int nbEnfants;

    // ── Situation professionnelle ─────────────────────────────────
    private String categorieSocioPro;
    private String revenu;
    private String natureActivite;
    private String secteurActivite;

    // ── Signature (PNG encodé base64) ─────────────────────────────
    private String signatureBase64;
}
