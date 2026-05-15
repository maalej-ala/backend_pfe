package pfe.backend.ocr;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OcrExtractionController {

    private final OcrExtractionService service;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OcrExtractionDTO create(

            @RequestParam String deviceId,

            @RequestParam(required = false) String nom,

            @RequestParam(required = false) String prenom,

            @RequestParam(required = false) String numeroCin,

            @RequestParam(required = false) String dateNaissance,

            @RequestParam(required = false) String dateExpiration,

            @RequestParam(required = false) String sexe,

            @RequestPart("photoCin") MultipartFile photoCin

    ) {

        OcrExtractionDTO dto = OcrExtractionDTO.builder()
                .deviceId(deviceId)
                .nom(nom)
                .prenom(prenom)
                .numeroCin(numeroCin)
                .dateNaissance(dateNaissance)
                .dateExpiration(dateExpiration)
                .sexe(sexe)
                .build();

        return service.create(dto, photoCin);
    }

    @GetMapping("/device/{deviceId}")
    public OcrExtractionDTO getByDeviceId(@PathVariable String deviceId) {
        return service.getByDeviceId(deviceId);
    }

    @GetMapping("/identification/{identificationId}")
    public OcrExtractionDTO getByIdentificationId(@PathVariable Long identificationId) {
        return service.getByIdentificationId(identificationId);
    }
}