package pfe.backend.adresse.model;

import jakarta.persistence.*;
import lombok.*;
import pfe.backend.identification.model.Identification;

@Entity
@Table(name = "adresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String adresse;

    @Column(name = "pays_nom", nullable = false)
    private String paysNom;

    @Column(nullable = false)
    private String gouvernorat;

    @Column(name = "code_postal", nullable = false, length = 10)
    private String codePostal;
    
    @OneToOne 
    @JoinColumn(name = "identification_id", nullable = false)
    private Identification identification;
}