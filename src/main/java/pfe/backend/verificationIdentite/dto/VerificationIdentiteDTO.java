package pfe.backend.verificationIdentite.dto;


import java.time.LocalDate;

import lombok.Data;

@Data
public class VerificationIdentiteDTO {
    private String cin;
    private LocalDate dateExpiration;
    private boolean estClientAutreBanque;
    private String deviceId; // 🔥 ajouté

}