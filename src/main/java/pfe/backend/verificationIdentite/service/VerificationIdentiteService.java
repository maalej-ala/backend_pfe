package pfe.backend.verificationIdentite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;
import pfe.backend.verificationIdentite.dto.OcrResultDTO;
import pfe.backend.verificationIdentite.dto.VerificationIdentiteDTO;
import pfe.backend.verificationIdentite.dto.VerificationResultDTO;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.repository.VerificationIdentiteRepository;

import java.io.IOException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationIdentiteService {

    private final VerificationIdentiteRepository repository;
    private final IdentificationRepository identificationRepository;
    private final OcrService ocrService;

    private static final String UPLOAD_DIR = "uploads/verification/";
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ══════════════════════════════════════════════════════════
    //  MÉTHODE PRINCIPALE
    // ══════════════════════════════════════════════════════════

    public VerificationResultDTO save(
            VerificationIdentiteDTO dto,
            MultipartFile photoCin,
            MultipartFile photoVisageCin,
            MultipartFile photoVisageLive
    ) throws IOException {

        // 1. Récupérer l'Identification via deviceId
        Identification identification = identificationRepository
                .findByDeviceId(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException(
                        "Utilisateur introuvable pour deviceId : " + dto.getDeviceId()));

        // 2. Sauvegarder les fichiers sur disque (inchangé)
        String photoCinPath        = saveFile(photoCin,        "cin_");
        String photoVisageCinPath  = saveFile(photoVisageCin,  "visage_cin_");
        String photoVisageLivePath = saveFile(photoVisageLive, "visage_live_");

        // 3. Persister VerificationIdentite
        VerificationIdentite entity = VerificationIdentite.builder()
                .cin(dto.getCin())
                .dateDelivrance(dto.getDateDelivrance())
                .estClientAutreBanque(dto.isEstClientAutreBanque())
                .photoCinPath(photoCinPath)
                .photoVisageCinPath(photoVisageCinPath)
                .photoVisageLivePath(photoVisageLivePath)
                .identification(identification)
                .build();

        VerificationIdentite saved = repository.save(entity);
        log.info("VerificationIdentite persistée : id={}", saved.getId());

        // 4. OCR : extraire les champs de la photo CIN (SANS vérification)
        OcrResultDTO ocrResult = null;
        if (photoCin != null && !photoCin.isEmpty()) {
            try {
                ocrResult = ocrService.extraireEtPersister(photoCin, saved);
                log.info("OCR extrait : numeroCin={} nom={} prenom={}",
                        ocrResult.getNumeroCin(),
                        ocrResult.getNom(),
                        ocrResult.getPrenom());
            } catch (Exception ex) {
                log.error("OCR échoué (non bloquant) : {}", ex.getMessage());
            }
        }

        // 5. Vérification croisée dans CE service
        return verifier(dto, identification, ocrResult);
    }

    // ══════════════════════════════════════════════════════════
    //  VÉRIFICATION CROISÉE
    //  Compare les champs saisis par l'utilisateur
    //  avec ceux extraits par l'OCR.
    // ══════════════════════════════════════════════════════════

    private VerificationResultDTO verifier(
            VerificationIdentiteDTO dto,
            Identification identification,
            OcrResultDTO ocr
    ) {
        VerificationResultDTO result = new VerificationResultDTO();

        // Données OCR (peut être null si OCR a échoué)
        result.setOcrExtrait(ocr);

        // Données saisies
        result.setCinSaisi(dto.getCin());
        result.setDateDelivranceSaisie(
                dto.getDateDelivrance() != null
                        ? dto.getDateDelivrance().format(DATE_FMT)
                        : null);

        if (ocr == null) {
            result.setIdentiteVerifiee(false);
            result.setMessage("OCR indisponible — vérification impossible");
            return result;
        }

        // ── Vérification CIN ─────────────────────────────────
        // Compare le CIN saisi par l'utilisateur avec celui extrait de la photo
        boolean cinValide = normalise(dto.getCin())
                .equals(normalise(ocr.getNumeroCin()));
        result.setCinValide(cinValide);
        result.setCinOcr(ocr.getNumeroCin());

        // ── Vérification Nom ─────────────────────────────────
        // Compare le nom enregistré lors de l'identification avec le nom OCR
        boolean nomValide = normaliseTexte(identification.getNom())
                .equals(normaliseTexte(ocr.getNom()));
        result.setNomValide(nomValide);
        result.setNomOcr(ocr.getNom());

        // ── Vérification Prénom ───────────────────────────────
        boolean prenomValide = normaliseTexte(identification.getPrenom())
                .equals(normaliseTexte(ocr.getPrenom()));
        result.setPrenomValide(prenomValide);
        result.setPrenomOcr(ocr.getPrenom());

        // ── Résultat global ───────────────────────────────────
        boolean identiteVerifiee = cinValide && nomValide && prenomValide;
        result.setIdentiteVerifiee(identiteVerifiee);

        // ── Message lisible ───────────────────────────────────
        if (identiteVerifiee) {
            result.setMessage("Identité vérifiée avec succès ✓");
        } else {
            StringBuilder msg = new StringBuilder("Vérification échouée : ");
            if (!cinValide)    msg.append("CIN incorrect; ");
            if (!nomValide)    msg.append("Nom incorrect; ");
            if (!prenomValide) msg.append("Prénom incorrect; ");
            result.setMessage(msg.toString().trim());
        }

        log.info("Vérification → cinValide={} nomValide={} prenomValide={} global={}",
                cinValide, nomValide, prenomValide, identiteVerifiee);

        return result;
    }

    // ══════════════════════════════════════════════════════════
    //  UTILITAIRES
    // ══════════════════════════════════════════════════════════

    /** Normalise un numéro CIN : enlève tirets/espaces, met en majuscules. */
    private String normalise(String s) {
        if (s == null) return "";
        return s.replaceAll("[\\s\\-]", "").toUpperCase();
    }

    /** Normalise un texte : minuscules, sans accents, sans espaces multiples. */
    private String normaliseTexte(String s) {
        if (s == null) return "";
        return java.text.Normalizer
                .normalize(s, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")   // enlève les accents
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }

    /** Lecture par ID (inchangée). */
    public VerificationIdentite getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "VerificationIdentite introuvable : id=" + id));
    }

    /** Sauvegarde d'un fichier sur disque (inchangée). */
    private String saveFile(MultipartFile file, String prefix) throws IOException {
        if (file == null || file.isEmpty()) return null;

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String originalName = file.getOriginalFilename();
        String extension = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf('.')) : ".jpg";

        String fileName = prefix + System.currentTimeMillis() + extension;
        Files.copy(file.getInputStream(),
                   uploadPath.resolve(fileName),
                   StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}