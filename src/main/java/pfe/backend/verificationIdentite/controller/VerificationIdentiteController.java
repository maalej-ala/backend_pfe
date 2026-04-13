package pfe.backend.verificationIdentite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfe.backend.verificationIdentite.dto.OcrResultDTO;
import pfe.backend.verificationIdentite.dto.VerificationIdentiteDTO;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.service.VerificationIdentiteService;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/verification-identite")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VerificationIdentiteController {

    private final VerificationIdentiteService service;

    /**
     * POST /api/verification-identite
     *
     * Reçoit les données + 3 fichiers → sauvegarde → OCR → retourne OcrResultDTO.
     *
     * Flutter récupère :
     *   - les champs extraits (nom, prenom, dateNaissance, numeroCin…)
     *   - cinValide : true si le CIN saisi = CIN extrait par OCR
     *   - messageVerification : message lisible pour l'UI
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OcrResultDTO> save(
            @RequestParam("cin")                  String cin,
            @RequestParam("dateDelivrance")       LocalDate dateDelivrance,
            @RequestParam("estClientAutreBanque") boolean estClientAutreBanque,
            @RequestParam("deviceId")             String deviceId,

            @RequestPart(value = "photoCin",        required = false) MultipartFile photoCin,
            @RequestPart(value = "photoVisageCin",  required = false) MultipartFile photoVisageCin,
            @RequestPart(value = "photoVisageLive", required = false) MultipartFile photoVisageLive
    ) throws IOException {

        VerificationIdentiteDTO dto = new VerificationIdentiteDTO();
        dto.setCin(cin);
        dto.setDateDelivrance(dateDelivrance);
        dto.setEstClientAutreBanque(estClientAutreBanque);
        dto.setDeviceId(deviceId);

        OcrResultDTO result = service.save(dto, photoCin, photoVisageCin, photoVisageLive);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/verification-identite/{id}
     * Retourne l'entité brute (inchangé).
     */
    @GetMapping("/{id}")
    public ResponseEntity<VerificationIdentite> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}