package pfe.backend.signature.model;

import jakarta.persistence.*;
import lombok.*;
import pfe.backend.identification.model.Identification;

@Entity
@Table(name = "signatures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "signature_base64", columnDefinition = "TEXT")
    private String signatureBase64;

    @OneToOne
    @JoinColumn(name = "identification_id", nullable = false, unique = true)
    private Identification identification;
}
