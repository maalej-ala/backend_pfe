package pfe.backend.admin.model;

import jakarta.persistence.*;
import lombok.*;
import pfe.backend.verificationIdentite.model.VerificationIdentite;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "demande_compte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandeCompte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score_similarite")
    private double scoreSimilarite;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    @Builder.Default
    private StatutDemande statut = StatutDemande.EN_ATTENTE;

    @OneToOne
    @JoinColumn(name = "verification_identite_id", nullable = false)
    private VerificationIdentite verificationIdentite;

    @OneToMany(mappedBy = "demandeCompte", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OcrDiscrepancy> discrepancesOcr = new ArrayList<>();
}
