package pfe.backend.signature.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfe.backend.adresse.model.Adresse;
import pfe.backend.adresse.repository.AdresseRepository;
import pfe.backend.admin.model.DemandeCompte;
import pfe.backend.admin.model.StatutDemande;
import pfe.backend.admin.repository.DemandeCompteRepository;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;
import pfe.backend.signature.dto.SignatureDTO;
import pfe.backend.signature.model.Signature;
import pfe.backend.signature.repository.SignatureRepository;
import pfe.backend.situationPersonnelle.model.SituationPersonnelle;
import pfe.backend.situationPersonnelle.repository.SituationPersonnelleRepository;
import pfe.backend.situationProfessionnelle.model.SituationProfessionnelle;
import pfe.backend.situationProfessionnelle.repository.SituationProfessionnelleRepository;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.repository.VerificationIdentiteRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignatureService {

    private final IdentificationRepository identificationRepository;
    private final VerificationIdentiteRepository verificationIdentiteRepository;
    private final AdresseRepository adresseRepository;
    private final SituationPersonnelleRepository situationPersonnelleRepository;
    private final SituationProfessionnelleRepository situationProfessionnelleRepository;
    private final SignatureRepository signatureRepository;
    private final DemandeCompteRepository demandeCompteRepository;

    // ══════════════════════════════════════════════════════════════
    //  GET /api/signature/{deviceId}
    //  Charge le récapitulatif complet pour Flutter
    // ══════════════════════════════════════════════════════════════
    @Transactional(readOnly = true)
    public SignatureDTO charger(String deviceId) {

        Identification ident = findIdentificationOrThrow(deviceId);
        VerificationIdentite verif = findVerificationOrThrow(ident);

        // Entités liées (optionnelles)
        Adresse adresse = adresseRepository.findByIdentification(ident).orElse(null);
        SituationPersonnelle sitPers = situationPersonnelleRepository.findByIdentification(ident).orElse(null);
        SituationProfessionnelle sitProf = situationProfessionnelleRepository.findByIdentification(ident).orElse(null);
        Signature sig = signatureRepository.findByIdentification(ident).orElse(null);

        SignatureDTO dto = new SignatureDTO();

        // ── Identité ──────────────────────────────────────────────
        dto.setCivilite(ident.getCivilite());
        dto.setNom(ident.getNom());
        dto.setPrenom(ident.getPrenom());
        dto.setEmail(ident.getEmail());
        dto.setTelephone(ident.getTelephone());
        dto.setDateNaissance(ident.getDateNaissance() != null ? ident.getDateNaissance().toString() : null);

        // ── CIN ───────────────────────────────────────────────────
        dto.setCin(ident.getCin());
        dto.setDateExpiration(ident.getDateExpiration() != null ? ident.getDateExpiration().toString() : null);
        dto.setEstClientAutreBanque(verif.getEstClientAutreBanque());

        // ── Adresse ───────────────────────────────────────────────
        if (adresse != null) {
            dto.setAdresse(adresse.getAdresse());
            dto.setPaysNom(adresse.getPaysNom());
            dto.setGouvernorat(adresse.getGouvernorat());
            dto.setCodePostal(adresse.getCodePostal());
        }

        // ── Situation personnelle ─────────────────────────────────
        if (sitPers != null) {
            dto.setNationalite(sitPers.getNationalite());
            dto.setStatutCivil(sitPers.getStatutCivil());
            dto.setNbEnfants(sitPers.getNbEnfants());
        }

        // ── Situation professionnelle ─────────────────────────────
        if (sitProf != null) {
            dto.setCategorieSocioPro(sitProf.getCategorieSocioPro());
            dto.setRevenu(sitProf.getRevenu());
            dto.setNatureActivite(sitProf.getNatureActivite());
            dto.setSecteurActivite(sitProf.getSecteurActivite());
        }

        // ── Signature ─────────────────────────────────────────────
        dto.setSignatureBase64(sig != null ? sig.getSignatureBase64() : null);

        log.info("Récapitulatif chargé pour deviceId={}", deviceId);
        return dto;
    }

    // ══════════════════════════════════════════════════════════════
    //  PUT /api/signature/{deviceId}
    //  Met à jour les champs modifiés par l'utilisateur
    // ══════════════════════════════════════════════════════════════
    @Transactional
    public void mettreAJour(String deviceId, SignatureDTO dto) {

        Identification ident = findIdentificationOrThrow(deviceId);
        VerificationIdentite verif = findVerificationOrThrow(ident);

        // ── Mise à jour Identification ────────────────────────────
        if (isPresent(dto.getCivilite())) ident.setCivilite(dto.getCivilite());
        if (isPresent(dto.getNom())) ident.setNom(dto.getNom());
        if (isPresent(dto.getPrenom())) ident.setPrenom(dto.getPrenom());
        if (isPresent(dto.getEmail())) ident.setEmail(dto.getEmail());
        if (isPresent(dto.getTelephone())) ident.setTelephone(dto.getTelephone());
        if (isPresent(dto.getDateNaissance())) {
            ident.setDateNaissance(parseDate(dto.getDateNaissance()));
        }
        identificationRepository.save(ident);

        // ── Mise à jour VerificationIdentite ──────────────────────
        if (isPresent(dto.getCin())) ident.setCin(dto.getCin());
        if (isPresent(dto.getDateExpiration())) {
            ident.setDateExpiration(parseDate(dto.getDateExpiration()));
        }
        verif.setEstClientAutreBanque(dto.isEstClientAutreBanque());
        verificationIdentiteRepository.save(verif);

        // ── Sauvegarde signature ──────────────────────────────────
        if (isPresent(dto.getSignatureBase64())) {
            Signature sig = signatureRepository
                    .findByIdentification(ident)
                    .orElse(Signature.builder().identification(ident).build());
            sig.setSignatureBase64(dto.getSignatureBase64());
            signatureRepository.save(sig);
        }

        log.info("Récapitulatif mis à jour pour deviceId={}", deviceId);
    }

    // ══════════════════════════════════════════════════════════════
    //  POST /api/signature/{deviceId}/soumettre
    //  Crée la DemandeCompte en statut EN_ATTENTE
    // ══════════════════════════════════════════════════════════════
    @Transactional
    public void soumettre(String deviceId) {

        Identification ident = findIdentificationOrThrow(deviceId);
        VerificationIdentite verif = findVerificationOrThrow(ident);

        // 1. Vérifier que la signature est présente
        Signature sig = signatureRepository.findByIdentification(ident)
                .orElseThrow(() -> new RuntimeException(
                        "Signature manquante — veuillez signer avant de soumettre."));

        if (!isPresent(sig.getSignatureBase64())) {
            throw new RuntimeException(
                    "La signature est obligatoire pour soumettre le dossier.");
        }

        // 2. Vérifier qu'aucune demande n'existe déjà pour ce dossier
        if (demandeCompteRepository.findByVerificationIdentite(verif).isPresent()) {
            throw new RuntimeException(
                    "Une demande a déjà été soumise pour ce dossier.");
        }

        // 3. Créer la DemandeCompte
        DemandeCompte demande = DemandeCompte.builder()
                .verificationIdentite(verif)
                .statut(StatutDemande.EN_ATTENTE)
                .scoreSimilarite(0.0)
                .build();

        DemandeCompte saved = demandeCompteRepository.save(demande);
        log.info("DemandeCompte créée : id={}, deviceId={}", saved.getId(), deviceId);
    }

    // ══════════════════════════════════════════════════════════════
    //  HELPERS PRIVÉS
    // ══════════════════════════════════════════════════════════════

    private Identification findIdentificationOrThrow(String deviceId) {
        return identificationRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException(
                        "Identification introuvable pour deviceId=" + deviceId));
    }

    private VerificationIdentite findVerificationOrThrow(Identification ident) {
        return verificationIdentiteRepository.findByIdentification(ident)
                .orElseThrow(() -> new RuntimeException(
                        "VerificationIdentite introuvable pour identificationId=" + ident.getId()));
    }

    /** Null-safe : retourne true si la chaîne est non nulle et non vide. */
    private boolean isPresent(String s) {
        return s != null && !s.isBlank();
    }

    /**
     * Parse une date en acceptant deux formats :
     *   - "yyyy-MM-dd"  (ISO, format LocalDate.toString())
     *   - "dd/MM/yyyy"  (format affiché dans Flutter)
     */
    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDate.parse(value); // yyyy-MM-dd
        } catch (DateTimeParseException e) {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    }
}
