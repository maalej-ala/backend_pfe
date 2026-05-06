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

    // ── Données CIN ──────────────────────────────────────────
    @Column(name = "cin", nullable = true, length = 15)
    private String cin;

    // @Column(name = "date_expiration", nullable = true)
    // private LocalDate dateDelivrance;

    @Column(name = "date_expiration", nullable = true)
    private LocalDate dateExpiration;

    @Column(name = "est_client_autre_banque", nullable = true)
    private Boolean estClientAutreBanque;

    // ── Chemins des fichiers stockés ─────────────────────────
    @Column(name = "photo_cin_path")
    private String photoCinPath;           // photo CIN (recto)

    @Column(name = "photo_visage_cin_path")
    private String photoVisageCinPath;     // visage extrait du CIN

    @Column(name = "photo_visage_live_path")
    private String photoVisageLivePath;    // photo visage live (frontFaceExtracted)
    
    @OneToOne
    @JoinColumn(name = "identification_id", nullable = false)
    private Identification identification;
}
