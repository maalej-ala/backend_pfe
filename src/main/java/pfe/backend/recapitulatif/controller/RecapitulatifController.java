// package pfe.backend.recapitulatif.controller;

package pfe.backend.recapitulatif.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.backend.recapitulatif.dto.RecapitulatifDTO;
import pfe.backend.recapitulatif.service.RecapitulatifService;

@RestController
@RequestMapping("/api/signature")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RecapitulatifController {

    private final RecapitulatifService service;

    /**
     * Retourne toutes les données du dossier sauf le mot de passe.
     * Identifié par le deviceId unique de l'appareil Flutter.
     */
    @GetMapping("/{deviceId}")
    public ResponseEntity<RecapitulatifDTO> getRecapitulatif(
            @PathVariable String deviceId) {
        return ResponseEntity.ok(service.getByDeviceId(deviceId));
    }
}