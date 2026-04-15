package pfe.backend.verificationIdentite.dto;

import lombok.Data;

/**
 * Ce que l'OCR retourne : uniquement les champs extraits de la photo CIN.
 * La vérification croisée (cinValide) est dans VerificationResultDTO.
 */
@Data
public class OcrResultDTO {
    // Champs extraits par Tesseract
    private String numeroCin;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String sexe;
    private String lieuNaissance;
    private String profession;
    private String dateEtablissement;
    private String dateExpiration;

    // Texte brut complet retourné par Tesseract (tel quel, sans modification)
    private String rawText;
}