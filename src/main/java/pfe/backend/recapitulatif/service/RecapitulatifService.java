// package pfe.backend.recapitulatif.service;

package pfe.backend.recapitulatif.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfe.backend.adresse.model.Adresse;
import pfe.backend.adresse.repository.AdresseRepository;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;
import pfe.backend.recapitulatif.dto.RecapitulatifDTO;
import pfe.backend.situationPersonnelle.model.SituationPersonnelle;
import pfe.backend.situationPersonnelle.repository.SituationPersonnelleRepository;
import pfe.backend.situationProfessionnelle.model.SituationProfessionnelle;
import pfe.backend.situationProfessionnelle.repository.SituationProfessionnelleRepository;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.repository.VerificationIdentiteRepository;

@Service
@RequiredArgsConstructor
public class RecapitulatifService {

    private final IdentificationRepository           identificationRepository;
    private final AdresseRepository                  adresseRepository;
    private final SituationPersonnelleRepository     situationPersonnelleRepository;
    private final SituationProfessionnelleRepository situationProfessionnelleRepository;
    private final VerificationIdentiteRepository     verificationIdentiteRepository;

    public RecapitulatifDTO getByDeviceId(String deviceId) {

        // 1. Identification (point d'entrée)
        Identification identification = identificationRepository
                .findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException(
                        "Identification introuvable pour deviceId : " + deviceId));

        // 2. Entités liées (optionnelles → null si pas encore saisies)
        Adresse adresse = adresseRepository
                .findByIdentification(identification)
                .orElse(null);

        SituationPersonnelle situationPersonnelle = situationPersonnelleRepository
                .findByIdentification(identification)
                .orElse(null);

        SituationProfessionnelle situationProfessionnelle = situationProfessionnelleRepository
                .findByIdentification(identification)
                .orElse(null);

        VerificationIdentite verificationIdentite = verificationIdentiteRepository
                .findByIdentification(identification)
                .orElse(null);

        // 3. Construction du DTO (sans mot de passe)
        return RecapitulatifDTO.builder()
                // Identification
                .civilite(identification.getCivilite())
                .nom(identification.getNom())
                .prenom(identification.getPrenom())
                .email(identification.getEmail())
                .telephone(identification.getTelephone())
                .dateNaissance(identification.getDateNaissance())
                // CIN
                .cin(verificationIdentite != null ? verificationIdentite.getCin() : null)
                .dateExpiration(verificationIdentite != null ? verificationIdentite.getDateExpiration() : null)
                .estClientAutreBanque(verificationIdentite != null && verificationIdentite.getEstClientAutreBanque())
                // Adresse
                .adresse(adresse != null ? adresse.getAdresse() : null)
                .paysNom(adresse != null ? adresse.getPaysNom() : null)
                .gouvernorat(adresse != null ? adresse.getGouvernorat() : null)
                .codePostal(adresse != null ? adresse.getCodePostal() : null)
                // Situation personnelle
                .nationalite(situationPersonnelle != null ? situationPersonnelle.getNationalite() : null)
                .statutCivil(situationPersonnelle != null ? situationPersonnelle.getStatutCivil() : null)
                .nbEnfants(situationPersonnelle != null ? situationPersonnelle.getNbEnfants() : 0)
                // Situation professionnelle
                .categorieSocioPro(situationProfessionnelle != null ? situationProfessionnelle.getCategorieSocioPro() : null)
                .revenu(situationProfessionnelle != null ? situationProfessionnelle.getRevenu() : null)
                .natureActivite(situationProfessionnelle != null ? situationProfessionnelle.getNatureActivite() : null)
                .secteurActivite(situationProfessionnelle != null ? situationProfessionnelle.getSecteurActivite() : null)
                .build();
    }
}