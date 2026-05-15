package pfe.backend.ocr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrExtractionService {

    private final OcrExtractionRepository ocrRepo;
    private final IdentificationRepository identificationRepo;

    public OcrExtractionDTO create(OcrExtractionDTO request,
            MultipartFile photoCin) {

        if (request.getDeviceId() == null || request.getDeviceId().isEmpty()) {
            throw new RuntimeException("deviceId requis");
        }

        // ── 1. Créer ou trouver Identification ───────────────────
        Identification identification = identificationRepo.findByDeviceId(request.getDeviceId())
                .orElse(null);

        if (identification == null) {
            // 🔥 CRÉER automatiquement Identification depuis OCR
            log.info("Création automatique d'Identification depuis OCR pour deviceId={}", request.getDeviceId());
            identification = Identification.builder()
                    .deviceId(request.getDeviceId())
                    .nom(request.getNom())                    // OCR → Identification
                    .prenom(request.getPrenom())              // OCR → Identification
                    .dateNaissance(parseDate(request.getDateNaissance())) // OCR → Identification
                    .civilite(mapSexeToCivilite(request.getSexe()))       // OCR sexe → civilite
                    .cin(request.getNumeroCin())              // OCR → CIN
                    .dateExpiration(parseDate(request.getDateExpiration())) // OCR → Date expiration
                    .accepteMentions(false)                   // défaut
                    .build();
            identification = identificationRepo.save(identification);
            log.info("Identification créée automatiquement : id={}", identification.getId());
        }
        String photoPath = savePhoto(photoCin);
        // ── 2. Créer ou mettre à jour l'extraction OCR ───────────
        OcrExtraction ocr = ocrRepo.findByIdentification(identification)
                .orElse(OcrExtraction.builder().identification(identification).build());

        ocr.setNom(request.getNom());
        ocr.setPrenom(request.getPrenom());
        ocr.setNumeroCin(request.getNumeroCin());
        ocr.setDateNaissance(parseDate(request.getDateNaissance()));
        ocr.setDateExpiration(parseDate(request.getDateExpiration()));
        ocr.setSexe(request.getSexe());
        ocr.setPhotoCinPath(photoPath);

        OcrExtraction saved = ocrRepo.save(ocr);
        log.info("OCR extraction sauvegardée : id={} pour deviceId={}", saved.getId(), identification.getDeviceId());

        // ── 3. Mettre à jour Identification avec les valeurs OCR ──
        // (seulement si Identification existait déjà)
        if (identificationRepo.findByDeviceId(request.getDeviceId()).isPresent()) {
            updateIdentificationFromOcr(identification, saved);
        }

        return toDTO(saved);
    }

    private String savePhoto(MultipartFile file) {

        try {

            String uploadDir = "uploads/cin/";

            File dir = new File(uploadDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path path = Paths.get(uploadDir, fileName);

            Files.write(path, file.getBytes());

            return path.toString();

        } catch (Exception e) {
            throw new RuntimeException("Erreur sauvegarde image", e);
        }
    }
    
    public OcrExtractionDTO getByDeviceId(String deviceId) {
        Identification identification = identificationRepo.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable pour deviceId: " + deviceId));

        OcrExtraction ocr = ocrRepo.findByIdentification(identification)
                .orElseThrow(() -> new RuntimeException("Aucune extraction OCR trouvée pour ce deviceId"));

        return toDTO(ocr);
    }

    public OcrExtractionDTO getByIdentificationId(Long identificationId) {
        Identification identification = identificationRepo.findById(identificationId)
                .orElseThrow(() -> new RuntimeException("Identification not found"));

        OcrExtraction ocr = ocrRepo.findByIdentification(identification)
                .orElseThrow(() -> new RuntimeException("Aucune extraction OCR trouvée pour cet utilisateur"));

        return toDTO(ocr);
    }

    // ── Mise à jour automatique de Identification ────────────
    private void updateIdentificationFromOcr(Identification identification, OcrExtraction ocr) {
        boolean updated = false;

        // Nom : mettre à jour si différent et OCR non vide
        if (isPresent(ocr.getNom()) && !ocr.getNom().equals(identification.getNom())) {
            log.info("Mise à jour nom : '{}' → '{}'", identification.getNom(), ocr.getNom());
            identification.setNom(ocr.getNom());
            updated = true;
        }

        // Prénom : mettre à jour si différent et OCR non vide
        if (isPresent(ocr.getPrenom()) && !ocr.getPrenom().equals(identification.getPrenom())) {
            log.info("Mise à jour prénom : '{}' → '{}'", identification.getPrenom(), ocr.getPrenom());
            identification.setPrenom(ocr.getPrenom());
            updated = true;
        }

        // Date de naissance : mettre à jour si différente et OCR non nulle
        if (ocr.getDateNaissance() != null && !ocr.getDateNaissance().equals(identification.getDateNaissance())) {
            log.info("Mise à jour date naissance : '{}' → '{}'", identification.getDateNaissance(), ocr.getDateNaissance());
            identification.setDateNaissance(ocr.getDateNaissance());
            updated = true;
        }

        // CIN : mettre à jour si différent et OCR non vide
        if (isPresent(ocr.getNumeroCin()) && !ocr.getNumeroCin().equals(identification.getCin())) {
            log.info("Mise à jour CIN : '{}' → '{}'", identification.getCin(), ocr.getNumeroCin());
            identification.setCin(ocr.getNumeroCin());
            updated = true;
        }

        // Date expiration : mettre à jour si différente et OCR non nulle
        if (ocr.getDateExpiration() != null && !ocr.getDateExpiration().equals(identification.getDateExpiration())) {
            log.info("Mise à jour date expiration : '{}' → '{}'", identification.getDateExpiration(), ocr.getDateExpiration());
            identification.setDateExpiration(ocr.getDateExpiration());
            updated = true;
        }

        // Civilité basée sur le sexe OCR (si disponible)
        if (isPresent(ocr.getSexe())) {
            String nouvelleCivilite = ocr.getSexe().equalsIgnoreCase("M") ? "M" : "Mme";
            if (!nouvelleCivilite.equals(identification.getCivilite())) {
                log.info("Mise à jour civilité : '{}' → '{}'", identification.getCivilite(), nouvelleCivilite);
                identification.setCivilite(nouvelleCivilite);
                updated = true;
            }
        }

        if (updated) {
            identificationRepo.save(identification);
            log.info("Identification mise à jour automatiquement depuis OCR");
        } else {
            log.info("Aucune mise à jour nécessaire pour Identification");
        }
    }

    // ── Utilitaires ───────────────────────────────────────────
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            log.warn("Impossible de parser la date : '{}'", dateStr);
            return null;
        }
    }

    private boolean isPresent(String s) {
        return s != null && !s.isBlank();
    }

    private String mapSexeToCivilite(String sexe) {
        if (sexe == null) return "M"; // défaut
        return sexe.equalsIgnoreCase("M") ? "M" : "Mme";
    }

    private OcrExtractionDTO toDTO(OcrExtraction ocr) {
        return OcrExtractionDTO.builder()
                .nom(ocr.getNom())
                .prenom(ocr.getPrenom())
                .numeroCin(ocr.getNumeroCin())
                .dateNaissance(ocr.getDateNaissance() != null ? ocr.getDateNaissance().toString() : null)
                .dateExpiration(ocr.getDateExpiration() != null ? ocr.getDateExpiration().toString() : null)
                .sexe(ocr.getSexe())
                .identificationId(ocr.getIdentification().getId())
                .deviceId(ocr.getIdentification().getDeviceId())
                .build();
    }
}