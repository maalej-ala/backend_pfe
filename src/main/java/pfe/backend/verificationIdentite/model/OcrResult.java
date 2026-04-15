package pfe.backend.verificationIdentite.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Stocke uniquement ce que Tesseract a extrait de la photo CIN.
 * Aucune logique de vérification ici.
 */
@Entity
@Table(name = "ocr_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cin")
    private String numeroCin;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_naissance")
    private String dateNaissance;

    @Column(name = "sexe", length = 1)
    private String sexe;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "profession")
    private String profession;

    @Column(name = "date_etablissement")
    private String dateEtablissement;

    @Column(name = "date_expiration")
    private String dateExpiration;

    // Texte brut complet retourné par Tesseract — tel quel, sans modification
    @Column(name = "raw_text", columnDefinition = "TEXT")
    private String rawText;

    @OneToOne
    @JoinColumn(name = "verification_identite_id")
    private VerificationIdentite verificationIdentite;
}