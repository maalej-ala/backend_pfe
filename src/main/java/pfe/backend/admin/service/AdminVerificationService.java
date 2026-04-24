// package pfe.backend.admin.service;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Service;
// import pfe.backend.admin.dto.AdminDossierDTO;
// import pfe.backend.admin.dto.AdminVerificationDTO;
// import pfe.backend.identification.model.Identification;
// import pfe.backend.identification.repository.IdentificationRepository;
// import pfe.backend.verificationIdentite.model.OcrResult;
// import pfe.backend.verificationIdentite.model.VerificationIdentite;
// import pfe.backend.verificationIdentite.repository.OcrResultRepository;
// import pfe.backend.verificationIdentite.repository.VerificationIdentiteRepository;

// import java.text.Normalizer;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class AdminVerificationService {

//     private final IdentificationRepository identificationRepository;
//     private final VerificationIdentiteRepository verificationIdentiteRepository;
//     private final OcrResultRepository ocrResultRepository;

//     // ── Liste tous les dossiers ───────────────────────────────
//     public List<AdminDossierDTO> getAllDossiers() {
//         return identificationRepository.findAll()
//                 .stream()
//                 .map(this::buildDossier)
//                 .collect(Collectors.toList());
//     }

//     // ── Dossier par deviceId ──────────────────────────────────
//     public AdminDossierDTO getDossierByDeviceId(String deviceId) {
//         Identification identification = identificationRepository
//                 .findByDeviceId(deviceId)
//                 .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + deviceId));
//         return buildDossier(identification);
//     }

//     // ── Dossier par identificationId ─────────────────────────
//     public AdminDossierDTO getDossierById(Long id) {
//         Identification identification = identificationRepository
//                 .findById(id)
//                 .orElseThrow(() -> new RuntimeException("Identification introuvable : " + id));
//         return buildDossier(identification);
//     }

//     // ── Construction du dossier complet ──────────────────────
//     private AdminDossierDTO buildDossier(Identification identification) {
//         Optional<VerificationIdentite> verifOpt =
//                 verificationIdentiteRepository.findByIdentification(identification);

//         OcrResult ocr = verifOpt
//                 .flatMap(v -> ocrResultRepository
//                         .findByVerificationIdentite(v))
//                 .orElse(null);

//         VerificationIdentite verif = verifOpt.orElse(null);

//         AdminVerificationDTO verification = comparer(identification, verif, ocr);

//         return AdminDossierDTO.builder()
//                 .identificationId(identification.getId())
//                 .deviceId(identification.getDeviceId())
//                 .civilite(identification.getCivilite())
//                 .nom(identification.getNom())
//                 .prenom(identification.getPrenom())
//                 .email(identification.getEmail())
//                 .telephone(identification.getTelephone())
//                 .dateNaissance(identification.getDateNaissance())
//                 .cin(verif != null ? verif.getCin() : null)
//                 .dateDelivrance(verif != null ? verif.getDateDelivrance() : null)
//                 .ocrNom(ocr != null ? ocr.getNom() : null)
//                 .ocrPrenom(ocr != null ? ocr.getPrenom() : null)
//                 .ocrNumeroCin(ocr != null ? ocr.getNumeroCin() : null)
//                 .ocrDateNaissance(ocr != null ? ocr.getDateNaissance() : null)
//                 .ocrDateExpiration(ocr != null ? ocr.getDateExpiration() : null)
//                 .ocrSexe(ocr != null ? ocr.getSexe() : null)
//                 .ocrRawText(ocr != null ? ocr.getRawText() : null)
//                 .verification(verification)
//                 .build();
//     }

//     // ── Comparaison champ par champ ───────────────────────────
//     private AdminVerificationDTO comparer(
//             Identification identification,
//             VerificationIdentite verif,
//             OcrResult ocr
//     ) {
//         if (verif == null || ocr == null) {
//             return AdminVerificationDTO.builder()
//                     .identiteVerifiee(false)
//                     .message("Données OCR ou vérification manquantes")
//                     .build();
//         }

//         // Données saisies
//         String nomSaisi    = identification.getNom();
//         String prenomSaisi = identification.getPrenom();
//         String cinSaisi    = verif.getCin();
//         String dateNaissanceSaisie = identification.getDateNaissance() != null
//                 ? identification.getDateNaissance().toString() : null;
//         // sexe et dateExpiration viennent uniquement de l'OCR (non saisis par l'utilisateur)
//         // mais on les expose pour info admin

//         // Comparaisons
//         boolean nomValide    = normaliseTexte(nomSaisi).equals(normaliseTexte(ocr.getNom()));
//         boolean prenomValide = normaliseTexte(prenomSaisi).equals(normaliseTexte(ocr.getPrenom()));
//         boolean cinValide    = normaliseCin(cinSaisi).equals(normaliseCin(ocr.getNumeroCin()));
//         boolean dateNaissanceValide = normaliseDate(dateNaissanceSaisie)
//                 .equals(normaliseDate(ocr.getDateNaissance()));

//         boolean identiteVerifiee = nomValide && prenomValide && cinValide && dateNaissanceValide;

//         StringBuilder msg = new StringBuilder();
//         if (identiteVerifiee) {
//             msg.append("Identité vérifiée avec succès");
//         } else {
//             msg.append("Vérification échouée : ");
//             if (!nomValide)           msg.append("Nom incorrect; ");
//             if (!prenomValide)        msg.append("Prénom incorrect; ");
//             if (!cinValide)           msg.append("CIN incorrect; ");
//             if (!dateNaissanceValide) msg.append("Date de naissance incorrecte; ");
//         }

//         log.info("Admin vérification → nom={} prenom={} cin={} dateNaissance={} global={}",
//                 nomValide, prenomValide, cinValide, dateNaissanceValide, identiteVerifiee);

//         return AdminVerificationDTO.builder()
//                 // Données saisies
//                 .nomSaisi(nomSaisi)
//                 .prenomSaisi(prenomSaisi)
//                 .numeroCinSaisi(cinSaisi)
//                 .dateNaissanceSaisie(dateNaissanceSaisie)
//                 .dateExpirationSaisie(verif.getDateDelivrance() != null
//                         ? verif.getDateDelivrance().toString() : null)
//                 .sexeSaisi(identification.getCivilite()) // civilite comme proxy du sexe
//                 // Données OCR
//                 .nomOcr(ocr.getNom())
//                 .prenomOcr(ocr.getPrenom())
//                 .numeroCinOcr(ocr.getNumeroCin())
//                 .dateNaissanceOcr(ocr.getDateNaissance())
//                 .dateExpirationOcr(ocr.getDateExpiration())
//                 .sexeOcr(ocr.getSexe())
//                 .rawTextOcr(ocr.getRawText())
//                 // Résultats
//                 .nomValide(nomValide)
//                 .prenomValide(prenomValide)
//                 .numeroCinValide(cinValide)
//                 .dateNaissanceValide(dateNaissanceValide)
//                 .dateExpirationValide(true) // date expiration non saisie → toujours OK
//                 .sexeValide(true)           // sexe non saisi → toujours OK
//                 .identiteVerifiee(identiteVerifiee)
//                 .message(msg.toString().trim())
//                 .build();
//     }

//     // ── Utilitaires ───────────────────────────────────────────

//     private String normaliseTexte(String s) {
//         if (s == null) return "";
//         return Normalizer.normalize(s, Normalizer.Form.NFD)
//                 .replaceAll("\\p{M}", "")
//                 .toLowerCase()
//                 .trim()
//                 .replaceAll("\\s+", " ");
//     }

//     private String normaliseCin(String s) {
//         if (s == null) return "";
//         return s.replaceAll("[\\s\\-]", "").toUpperCase();
//     }

//     private String normaliseDate(String s) {
//         if (s == null) return "";
//         // Normalise les séparateurs : 01/01/1990 → 01-01-1990
//         return s.replaceAll("[/\\s]", "-").trim();
//     }
// }
