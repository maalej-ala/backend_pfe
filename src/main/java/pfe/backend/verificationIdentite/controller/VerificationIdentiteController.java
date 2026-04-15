package pfe.backend.verificationIdentite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfe.backend.verificationIdentite.dto.VerificationIdentiteDTO;
import pfe.backend.verificationIdentite.dto.VerificationResultDTO;
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
     * Flux :
     *   1. Sauvegarde les 3 photos
     *   2. Lance l'OCR sur photoCin → extrait les champs bruts
     *   3. Compare les champs saisis avec les champs OCR
     *   4. Retourne VerificationResultDTO avec :
     *        - ocrExtrait       : ce que la carte dit (rawText + champs)
     *        - cinValide        : CIN saisi == CIN carte
     *        - nomValide        : nom identification == nom carte
     *        - prenomValide     : prénom identification == prénom carte
     *        - identiteVerifiee : true si tout correspond
     *        - message          : texte lisible pour l'UI Flutter
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VerificationResultDTO> save(
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

        VerificationResultDTO result = service.save(
                dto, photoCin, photoVisageCin, photoVisageLive);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /api/verification-identite/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VerificationIdentite> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}