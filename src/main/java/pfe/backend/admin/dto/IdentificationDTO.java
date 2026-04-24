package pfe.backend.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdentificationDTO {
    private String civilite;
    private boolean accepteMentions;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String dateNaissance;
}
