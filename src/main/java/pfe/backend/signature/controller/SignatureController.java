package pfe.backend.signature.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.backend.signature.dto.SignatureDTO;
import pfe.backend.signature.service.SignatureService;

@RestController
@RequestMapping("/api/signature")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SignatureController {

    private final SignatureService service;

    /**
     * GET /api/signature/{deviceId}
     *
     * Appelé par Flutter au chargement de SignaturePage.
     * Retourne le récapitulatif complet (Identification + VerificationIdentite
     * + signature existante si déjà signée).
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<SignatureDTO> charger(@PathVariable String deviceId) {
        return ResponseEntity.ok(service.charger(deviceId));
    }

    /**
     * PUT /api/signature/{deviceId}
     *
     * Appelé par Flutter via _SignatureEditService.mettreAJour().
     * Persiste les champs modifiés + la signature base64.
     */
    @PutMapping("/{deviceId}")
    public ResponseEntity<Void> mettreAJour(
            @PathVariable String deviceId,
            @RequestBody SignatureDTO dto
    ) {
        service.mettreAJour(deviceId, dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/signature/{deviceId}/soumettre
     *
     * Appelé par Flutter via _SignatureEditService.soumettre()
     * après mettreAJour() (= confirmerEtTerminer).
     *
     * Crée la DemandeCompte en statut EN_ATTENTE.
     * Le scoreSimilarite sera mis à jour ultérieurement par le service Python/ML.
     */
    @PostMapping("/{deviceId}/soumettre")
    public ResponseEntity<Void> soumettre(@PathVariable String deviceId) {
        service.soumettre(deviceId);
        return ResponseEntity.ok().build();
    }
}
