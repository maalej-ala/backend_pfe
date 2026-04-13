package pfe.backend.adresse.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdresseDTO {

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;


    @NotBlank(message = "Le nom du pays est obligatoire")
    private String paysNom;

    @NotBlank(message = "Le gouvernorat est obligatoire")
    private String gouvernorat;

    @NotBlank(message = "Le code postal est obligatoire")
    @Pattern(regexp = "^\\d{1,10}$", message = "Code postal invalide")////////////VERIFY regexp
    private String codePostal;
    
    private String deviceId; // 🔥 ajouté

}