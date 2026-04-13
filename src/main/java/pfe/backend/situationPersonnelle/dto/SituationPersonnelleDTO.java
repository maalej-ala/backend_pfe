package pfe.backend.situationPersonnelle.dto;


import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class SituationPersonnelleDTO {

    private String nationalite;

    private String statutCivil;

    @Min(value = 0, message = "Le nombre d'enfants ne peut pas être négatif")
    private int nbEnfants = 0;
    private String deviceId; // 🔥 ajouté

}
