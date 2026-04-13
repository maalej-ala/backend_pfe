package pfe.backend.verificationIdentite.dto;


import java.time.LocalDate;

import lombok.Data;

@Data
public class VerificationIdentiteDTO {
    private String cin;
    private LocalDate dateDelivrance;
    private boolean estClientAutreBanque;
    private String deviceId; // 🔥 ajouté

}