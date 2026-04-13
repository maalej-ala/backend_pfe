package pfe.backend.identification.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentificationRequest {

    @NotBlank(message = "La civilité est obligatoire")
    private String civilite;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(
        regexp = "^\\+[1-9][0-9]{7,14}$",
        message = "Numéro invalide"
    )
    private String telephone;

    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date doit être dans le passé")
    private LocalDate dateNaissance;

    @AssertTrue(message = "Vous devez accepter les mentions légales")
    private boolean accepteMentions;
    
    @NotBlank(message = "Le deviceId est obligatoire")
    private String deviceId;
}