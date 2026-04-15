package pfe.backend.verificationIdentite.dto;

import lombok.Data;

/**
 * Réponse finale envoyée à Flutter après POST /api/verification-identite.
 * Contient :
 *   1. Les champs extraits par l'OCR (ce que la carte dit)
 *   2. Les champs saisis par l'utilisateur (ce qu'il a entré)
 *   3. Le résultat de la vérification croisée champ par champ
 */
@Data
public class VerificationResultDTO {

    // ── Ce que l'OCR a extrait (texte brut + champs) ─────────
    private OcrResultDTO ocrExtrait;

    // ── Ce que l'utilisateur a saisi ─────────────────────────
    private String cinSaisi;
    private String dateDelivranceSaisie; // format "dd-MM-yyyy"

    // ── Résultats de la vérification champ par champ ─────────
    private boolean cinValide;            // numeroCin OCR == cinSaisi
    private boolean nomValide;            // nom OCR == nom Identification
    private boolean prenomValide;         // prenom OCR == prenom Identification

    // Résultat global : true si TOUS les champs clés sont valides
    private boolean identiteVerifiee;

    // Message lisible pour l'UI Flutter
    private String message;

    // ── Détails des écarts (utile pour debug / affichage) ─────
    private String cinOcr;      // valeur brute extraite par OCR
    private String nomOcr;
    private String prenomOcr;
}