package pfe.backend.adresse.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pfe.backend.adresse.dto.AdresseDTO;
import pfe.backend.adresse.model.Adresse;
import pfe.backend.adresse.service.AdresseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adresse")
@CrossOrigin(origins = "*") // autorise Flutter
@RequiredArgsConstructor
public class AdresseController {

    private final AdresseService adresseService;

    @PostMapping
    public ResponseEntity<Adresse> saveAdresse(@Valid @RequestBody AdresseDTO dto ) {
        Adresse saved = adresseService.saveAdresse(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adresse> getAdresse(@PathVariable Long id) {
        return ResponseEntity.ok(adresseService.getAdresseById(id));
    }
}