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

    private String civilite;

    private String nom;

    private String prenom;

    @Email(message = "Email invalide")
    private String email;

    @Pattern(
        regexp = "^\\+[1-9][0-9]{7,14}$",
        message = "Numéro invalide"
    )
    private String telephone;

    @Past(message = "La date doit être dans le passé")
    private LocalDate dateNaissance;

    private Boolean accepteMentions;
    
    @NotBlank(message = "Le deviceId est obligatoire")
    private String deviceId;


     @Pattern(
        regexp = "^[A-Z0-9\\-]{5,30}$",
        message = "Numéro de carte invalide"
    )
    private String numero;

    @Future(message = "La date d'expiration doit être dans le futur")
    private LocalDate dateExpiration;
}