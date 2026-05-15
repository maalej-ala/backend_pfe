package pfe.backend.ocr;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrExtractionDTO {

    private String nom;

    private String prenom;

    private String numeroCin;

    private String dateNaissance;

    private String dateExpiration;

    private String sexe;
    
    private String deviceId;
    
    private String photoCinPath;
    
    private Long identificationId;

}