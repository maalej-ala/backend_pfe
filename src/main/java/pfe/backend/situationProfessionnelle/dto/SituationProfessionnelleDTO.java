package pfe.backend.situationProfessionnelle.dto;

import lombok.Data;

@Data
public class SituationProfessionnelleDTO {

    private String categorieSocioPro;

    private String revenu;

    private String natureActivite;

    private String secteurActivite;
    private String deviceId; // 🔥 ajouté

}