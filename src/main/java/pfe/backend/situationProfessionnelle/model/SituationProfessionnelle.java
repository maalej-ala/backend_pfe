package pfe.backend.situationProfessionnelle.model;


import jakarta.persistence.*;
import lombok.*;
import pfe.backend.identification.model.Identification;

@Entity
@Table(name = "situation_professionnelle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SituationProfessionnelle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categorie_socio_pro")
    private String categorieSocioPro;

    @Column(name = "revenu")
    private String revenu;

    @Column(name = "nature_activite")
    private String natureActivite;

    @Column(name = "secteur_activite")
    private String secteurActivite;
    
    @OneToOne
    @JoinColumn(name = "identification_id", nullable = false)
    private Identification identification;
}
