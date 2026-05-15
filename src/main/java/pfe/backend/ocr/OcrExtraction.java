package pfe.backend.ocr;


import jakarta.persistence.*;
import lombok.*;
import pfe.backend.identification.model.Identification;

import java.time.LocalDate;
@Entity
@Table(name = "ocr_extraction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrExtraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String prenom;

    @Column(name = "numero_cin")
    private String numeroCin;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    private String sexe;
    
    @Column(name = "photo_cin_path")
    private String photoCinPath;
    
    @OneToOne
    @JoinColumn(name = "identification_id")
    private Identification identification;
}
