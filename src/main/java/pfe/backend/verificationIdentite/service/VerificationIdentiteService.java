package pfe.backend.verificationIdentite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;
import pfe.backend.verificationIdentite.dto.VerificationIdentiteDTO;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.repository.VerificationIdentiteRepository;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationIdentiteService {

    private final VerificationIdentiteRepository repository;
    private final IdentificationRepository identificationRepository;

    private static final String UPLOAD_DIR = "uploads/verification/";

    // ══════════════════════════════════════════════════════════
    //  MÉTHODE PRINCIPALE
    //  1. Récupère l'Identification via deviceId
    //  2. Sauvegarde les 3 photos sur disque
    //  3. Persiste et retourne VerificationIdentite
    // ══════════════════════════════════════════════════════════
    public VerificationIdentite save(
            VerificationIdentiteDTO dto,
            MultipartFile photoCin,
            MultipartFile photoVisageCin,
            MultipartFile photoVisageLive
    ) throws IOException {

        // 1. Récupérer l'Identification liée au device
        Identification identification = identificationRepository
                .findByDeviceId(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException(
                        "Utilisateur introuvable pour deviceId : " + dto.getDeviceId()));

        // 2. Sauvegarder les photos sur disque
        String photoCinPath        = saveFile(photoCin,        "cin_");
        String photoVisageCinPath  = saveFile(photoVisageCin,  "visage_cin_");
        String photoVisageLivePath = saveFile(photoVisageLive, "visage_live_");

        // 3. Construire et persister l'entité
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
        log.info("VerificationIdentite persistée : id={} cin={}", saved.getId(), saved.getCin());

        return saved;
    }

    // ══════════════════════════════════════════════════════════
    //  GET PAR ID
    // ══════════════════════════════════════════════════════════
    public VerificationIdentite getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "VerificationIdentite introuvable : id=" + id));
    }

    // ══════════════════════════════════════════════════════════
    //  UTILITAIRE : sauvegarde d'un fichier sur disque
    // ══════════════════════════════════════════════════════════
    private String saveFile(MultipartFile file, String prefix) throws IOException {
        if (file == null || file.isEmpty()) return null;

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String originalName = file.getOriginalFilename();
        String extension = (originalName != null && originalName.contains("."))
                ? originalName.substring(originalName.lastIndexOf('.'))
                : ".jpg";

        String fileName = prefix + System.currentTimeMillis() + extension;
        Files.copy(
                file.getInputStream(),
                uploadPath.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING
        );

        log.debug("Photo sauvegardée : {}{}", UPLOAD_DIR, fileName);
        return fileName;
    }
}