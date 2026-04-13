// package pfe.backend.motDePasse.controller;

package pfe.backend.motDePasse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.backend.motDePasse.dto.MotDePasseDTO;
import pfe.backend.motDePasse.model.MotDePasse;
import pfe.backend.motDePasse.service.MotDePasseService;

@RestController
@RequestMapping("/api/mot-de-passe")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MotDePasseController {

    private final MotDePasseService service;

    @PostMapping
    public ResponseEntity<MotDePasse> save(@Valid @RequestBody MotDePasseDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotDePasse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}