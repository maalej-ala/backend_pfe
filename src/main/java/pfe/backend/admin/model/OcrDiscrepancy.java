package pfe.backend.admin.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ocr_discrepancy")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrDiscrepancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "champ", nullable = false)
    private String champ;

    @Column(name = "valeur_ocr")
    private String valeurOcr;

    @ManyToOne
    @JoinColumn(name = "demande_compte_id", nullable = false)
    private DemandeCompte demandeCompte;
}
