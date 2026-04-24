package pfe.backend.admin.dto;

import lombok.Builder;
import lombok.Data;


/**
 * Réponse pour GET /api/admin/demandes et GET /api/admin/demandes/{id}
 * Structure miroir du modèle Flutter DemandeCompte.
 */
@Data
@Builder
public class AdminDossierDTO {

    private Long id;

    private String nom;
    private String prenom;
    private String telephone;
    private String email;

    private String cin;

    private double scoreSimilarite;
    private String statut;
}