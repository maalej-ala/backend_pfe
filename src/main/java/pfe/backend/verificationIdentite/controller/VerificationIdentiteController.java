package pfe.backend.verificationIdentite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfe.backend.verificationIdentite.dto.VerificationIdentiteDTO;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.service.VerificationIdentiteService;

import java.io.IOException;

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
     *   1. Sauvegarde les 3 photos sur disque
     *   2. Persiste VerificationIdentite en base
     *   3. Retourne directement l'entité sauvegardée
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VerificationIdentite> save(
            @RequestParam("estClientAutreBanque") boolean estClientAutreBanque,
            @RequestParam("deviceId")             String deviceId,
            @RequestPart(value = "photoVisageLive", required = false) MultipartFile photoVisageLive
    ) throws IOException {

        VerificationIdentiteDTO dto = new VerificationIdentiteDTO();
        dto.setEstClientAutreBanque(estClientAutreBanque);
        dto.setDeviceId(deviceId);

        VerificationIdentite saved = service.save(dto, photoVisageLive);
        return ResponseEntity.ok(saved);
    }

    /**
     * GET /api/verification-identite/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VerificationIdentite> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

     @GetMapping("/device/{deviceId}")
    public ResponseEntity<VerificationIdentite> getByDeviceId(
            @PathVariable String deviceId) {

        VerificationIdentite result = service.getByDeviceId(deviceId);
        return ResponseEntity.ok(result);
    }
}