package pfe.backend.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrDiscrepancyDTO {
    private String champ;
    private String valeurUtilisateur;
    private String valeurOcr;
}
