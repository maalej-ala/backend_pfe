package pfe.backend.verificationIdentite.service;


import lombok.RequiredArgsConstructor;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;
import pfe.backend.verificationIdentite.dto.VerificationIdentiteDTO;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.repository.VerificationIdentiteRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
@RequiredArgsConstructor
public class VerificationIdentiteService {

    private final VerificationIdentiteRepository repository;
    private final IdentificationRepository identificationRepository;

    
    // Dossier de stockage des fichiers
    private static final String UPLOAD_DIR = "uploads/verification/";

    // ── Sauvegarde principale ────────────────────────────────
    public VerificationIdentite save(
            VerificationIdentiteDTO dto,
            MultipartFile photoCin,
            MultipartFile photoVisageCin,
            MultipartFile photoVisageLive
    ) throws IOException {

        // 🔥 récupérer l'utilisateur via deviceId
        Identification identification = identificationRepository
                .findByDeviceId(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        // 1. Sauvegarder les fichiers sur disque
        String photoCinPath       = saveFile(photoCin,       "cin_");
        String photoVisageCinPath = saveFile(photoVisageCin, "visage_cin_");
        String photoVisageLivePath = saveFile(photoVisageLive, "visage_live_");

        // 2. Construire et persister l'entité
        VerificationIdentite entity = VerificationIdentite.builder()
                .cin(dto.getCin())
                .dateDelivrance(dto.getDateDelivrance())
                .estClientAutreBanque(dto.isEstClientAutreBanque())
                .photoCinPath(photoCinPath)
                .photoVisageCinPath(photoVisageCinPath)
                .photoVisageLivePath(photoVisageLivePath)
                .identification(identification)
                .build();

        return repository.save(entity);
    }
    
    public VerificationIdentite getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "VerificationIdentite introuvable avec l'id : " + id));
    }

    // ── Utilitaire : écriture fichier sur disque ─────────────
    private String saveFile(MultipartFile file, String prefix) throws IOException {
        if (file == null || file.isEmpty()) return null;

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        String fileName = prefix + System.currentTimeMillis() + extension;

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName; // 🔥 stocker juste le nom (pas tout le path)
    }
    }
