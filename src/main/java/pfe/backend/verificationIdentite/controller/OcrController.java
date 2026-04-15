package pfe.backend.verificationIdentite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pfe.backend.verificationIdentite.dto.OcrResultDTO;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.service.OcrService;
import pfe.backend.verificationIdentite.service.VerificationIdentiteService;

import java.io.IOException;

@RestController
@RequestMapping("/api/ocr")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OcrController {

    private final OcrService ocrService;
    private final VerificationIdentiteService verificationIdentiteService;

    /**
     * POST /api/ocr/analyser
     *
     * Reçoit la photo CIN + le CIN saisi par l'utilisateur,
     * effectue l'OCR côté serveur, croise avec le CIN saisi,
     * et retourne les données extraites + résultat de vérification.
     *
     * Paramètres multipart/form-data :
     *   - photoCin          (file)    : image recto de la CIN
     *   - cinSaisi          (text)    : numéro CIN saisi par l'utilisateur
     *   - verificationId    (text)    : ID de la VerificationIdentite parente
     */
    @PostMapping(
        value = "/analyser",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<OcrResultDTO> analyser(
            @RequestPart("photoCin")        MultipartFile photoCin,
            @RequestParam("verificationId") Long verificationId
    ) throws IOException {

        VerificationIdentite verif = verificationIdentiteService.getById(verificationId);

        // Lancer l'OCR + parsing + vérification + persistance
        OcrResultDTO result = ocrService.extraireEtPersister(photoCin, verif);

        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/ocr/analyser-direct
     *
     * Version simplifiée sans lien vers VerificationIdentite.
     * Utile pour tester l'OCR indépendamment.
     *
     * Paramètres multipart/form-data :
     *   - photoCin  (file) : image de la CIN
     */
    @PostMapping(
        value = "/analyser-direct",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<OcrResultDTO> analyserDirect(
            @RequestPart("photoCin") MultipartFile photoCin
    ) throws IOException {

        OcrResultDTO result = ocrService.extraireEtPersister(photoCin, null);
        return ResponseEntity.ok(result);
    }
}