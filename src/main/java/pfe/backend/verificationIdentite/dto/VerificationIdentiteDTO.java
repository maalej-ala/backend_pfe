package pfe.backend.verificationIdentite.dto;



import lombok.Data;

@Data
public class VerificationIdentiteDTO {
    private boolean estClientAutreBanque;
    private String deviceId; // 🔥 ajouté

}