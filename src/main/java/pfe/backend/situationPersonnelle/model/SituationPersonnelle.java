package pfe.backend.situationPersonnelle.model;

import jakarta.persistence.*;
import lombok.*;
import pfe.backend.identification.model.Identification;

@Entity
@Table(name = "situation_personnelle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SituationPersonnelle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "statut_civil")
    private String statutCivil;

    @Column(name = "nb_enfants", nullable = false)
    private int nbEnfants = 0;
    
    @OneToOne
    @JoinColumn(name = "identification_id", nullable = false)
    private Identification identification;
}