package pfe.backend.verificationIdentite.model;


import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import pfe.backend.identification.model.Identification;

@Entity
@Table(name = "verification_identite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationIdentite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "est_client_autre_banque", nullable = true)
    private Boolean estClientAutreBanque;

    @Column(name = "photo_visage_live_path")
    private String photoVisageLivePath;    // photo visage live (frontFaceExtracted)
    
    @OneToOne
    @JoinColumn(name = "identification_id", nullable = false)
    private Identification identification;
}
