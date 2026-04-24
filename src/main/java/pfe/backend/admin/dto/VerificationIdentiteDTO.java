package pfe.backend.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationIdentiteDTO {
    private Long id;
    private String cin;
    private String dateDelivrance;
    private boolean estClientAutreBanque;
    private String photoCinPath;
    private String photoVisageCinPath;
    private String photoVisageLivePath;
    private IdentificationDTO identification;
}
