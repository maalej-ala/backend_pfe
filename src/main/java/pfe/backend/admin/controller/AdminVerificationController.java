//package pfe.backend.admin.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import pfe.backend.admin.dto.AdminDossierDTO;
//import pfe.backend.admin.service.AdminVerificationService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin/verification")
//@CrossOrigin(origins = "*")
//@RequiredArgsConstructor
//public class AdminVerificationController {
//
//    private final AdminVerificationService service;
//
//    /**
//     * GET /api/admin/verification
//     * Liste tous les dossiers avec comparaison OCR vs données saisies.
//     */
//    // @GetMapping
//    // public ResponseEntity<List<AdminDossierDTO>> getAllDossiers() {
//    //     return ResponseEntity.ok(service.getAllDossiers());
//    // }
//
//    // /**
//    //  * GET /api/admin/verification/{id}
//    //  * Dossier par identificationId.
//    //  */
//    // @GetMapping("/{id}")
//    // public ResponseEntity<AdminDossierDTO> getDossierById(@PathVariable Long id) {
//    //     return ResponseEntity.ok(service.getDossierById(id));
//    // }
//
//    // /**
//    //  * GET /api/admin/verification/device/{deviceId}
//    //  * Dossier par deviceId.
//    //  */
//    // @GetMapping("/device/{deviceId}")
//    // public ResponseEntity<AdminDossierDTO> getDossierByDeviceId(@PathVariable String deviceId) {
//    //     return ResponseEntity.ok(service.getDossierByDeviceId(deviceId));
//    // }
//}
