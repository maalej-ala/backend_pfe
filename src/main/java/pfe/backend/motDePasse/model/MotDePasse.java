// package pfe.backend.motDePasse.model;

package pfe.backend.motDePasse.model;

import jakarta.persistence.*;
import lombok.*;
import pfe.backend.identification.model.Identification;

@Entity
@Table(name = "mot_de_passe")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotDePasse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;
    
    @OneToOne
    @JoinColumn(name = "identification_id", nullable = false)
    private Identification identification;
}