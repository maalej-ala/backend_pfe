package pfe.backend.situationPersonnelle.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pfe.backend.situationPersonnelle.dto.SituationPersonnelleDTO;
import pfe.backend.situationPersonnelle.model.SituationPersonnelle;
import pfe.backend.situationPersonnelle.service.SituationPersonnelleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/situation-personnelle")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SituationPersonnelleController {

    private final SituationPersonnelleService service;

    @PostMapping
    public ResponseEntity<SituationPersonnelle> save(
            @Valid @RequestBody SituationPersonnelleDTO dto) {
        return ResponseEntity.ok(service.createOrUpdate(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SituationPersonnelle> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
