package pfe.backend.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pfe.backend.admin.dto.AdminDossierDTO;
import pfe.backend.admin.service.AdminDemandeService;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminDemandeController {

    private final AdminDemandeService service;

    private static final String UPLOAD_DIR = "uploads/verification/";

    /**
     * GET /api/admin/demandes
     * Retourne toutes les demandes de création de compte.
     */
    @GetMapping("/demandes")
    public ResponseEntity<List<AdminDossierDTO>> getAllDemandes() {
        return ResponseEntity.ok(service.getAllDemandes());
    }

    /**
     * GET /api/admin/demandes/{id}
     * Retourne le détail d'une demande.
     */
//    @GetMapping("/demandes/{id}")
//    public ResponseEntity<AdminDossierDTO> getDemandeById(@PathVariable Long id) {
//        return ResponseEntity.ok(service.getDemandeById(id));
//    }

    /**
     * GET /api/admin/images?path=xxx
     * Sert les images (photo CIN, visage CIN, visage live).
     */
    // @GetMapping("/images")
    // public ResponseEntity<Resource> getImage(@RequestParam String path) {
    //     try {
    //         Path filePath = Paths.get(UPLOAD_DIR).resolve(path).normalize();
    //         Resource resource = new UrlResource(filePath.toUri());

    //         if (!resource.exists()) {
    //             return ResponseEntity.notFound().build();
    //         }

    //         String contentType = path.toLowerCase().endsWith(".png")
    //                 ? MediaType.IMAGE_PNG_VALUE : MediaType.IMAGE_JPEG_VALUE;

    //         return ResponseEntity.ok()
    //                 .contentType(MediaType.parseMediaType(contentType))
    //                 .body(resource);

    //     } catch (MalformedURLException e) {
    //         return ResponseEntity.badRequest().build();
    //     }
    // }
}
