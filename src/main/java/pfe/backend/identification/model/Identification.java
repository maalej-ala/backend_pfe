package pfe.backend.identification.model;

import jakarta.persistence.*;
import lombok.*;
import pfe.backend.adresse.model.Adresse;
import pfe.backend.motDePasse.model.MotDePasse;
import pfe.backend.situationPersonnelle.model.SituationPersonnelle;
import pfe.backend.situationProfessionnelle.model.SituationProfessionnelle;

import java.time.LocalDate;

@Entity
@Table(name = "identifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Identification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String civilite;        // "M" ou "Mme"

    @Column(nullable = true)
    private String nom;

    @Column(nullable = true)
    private String prenom;
//  unique = true + null
//  En général, les bases comme PostgreSQL ou MySQL permettent plusieurs valeurs NULL même avec unique
    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String telephone;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;
    
    @Builder.Default
    private Boolean accepteMentions = false;

    @Column(name = "device_id", nullable = false, unique = true)
    private String deviceId;  // ⚠️ unique par appareil
    
    // ── Champs CIN (ajoutés depuis OCR) ───────────────────────
    @Column(name = "cin", length = 20)
    private String cin;                    // Numéro CIN
    
    @Column(name = "date_expiration")
    private LocalDate dateExpiration;      // Date expiration CIN
    
   // @OneToOne(mappedBy = "identification", cascade = CascadeType.ALL)
   // private Adresse adresse;
    

    // Relation OneToOne avec Profession (exemple)
  //  @OneToOne(mappedBy = "identification", cascade = CascadeType.ALL)
   // private SituationProfessionnelle profession;
    
  //  @OneToOne(mappedBy = "identification", cascade = CascadeType.ALL)
  //  private MotDePasse motDePasse ;
    
  //  @OneToOne(mappedBy = "identification", cascade = CascadeType.ALL)
  //  private SituationPersonnelle situationPersonnelle ;
    
    
}
