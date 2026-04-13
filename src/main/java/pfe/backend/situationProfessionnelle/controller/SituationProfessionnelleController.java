package pfe.backend.situationProfessionnelle.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pfe.backend.situationProfessionnelle.dto.SituationProfessionnelleDTO;
import pfe.backend.situationProfessionnelle.model.SituationProfessionnelle;
import pfe.backend.situationProfessionnelle.service.SituationProfessionnelleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/situation-professionnelle")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SituationProfessionnelleController {

    private final SituationProfessionnelleService service;

    @PostMapping
    public ResponseEntity<SituationProfessionnelle> save(
            @Valid @RequestBody SituationProfessionnelleDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SituationProfessionnelle> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}