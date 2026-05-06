package pfe.backend.identification.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.backend.identification.dto.IdentificationRequest;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.service.IdentificationService;

@RestController
@RequestMapping("/api/identification")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")   // À restreindre en production
public class IdentificationController {

    private final IdentificationService service;

    @PostMapping
    public ResponseEntity<Identification> createIdentification(
            @Valid @RequestBody IdentificationRequest request) {

        Identification saved = service.saveOrUpdateIdentification(request);
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping("/{deviceId}")
    public ResponseEntity<Identification> getByDeviceId(@PathVariable String deviceId) {

        Identification identification = service.getByDeviceId(deviceId);

        return ResponseEntity.ok(identification);
    }
}