package pfe.backend.ocr;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OcrExtractionController {

    private final OcrExtractionService service;

    @PostMapping
    public OcrExtractionDTO create(@RequestBody OcrExtractionDTO request) {
        return service.create(request);
    }

    @GetMapping("/device/{deviceId}")
    public OcrExtractionDTO getByDeviceId(@PathVariable String deviceId) {
        return service.getByDeviceId(deviceId);
    }

    // Garder l'ancien endpoint pour compatibilité
    @GetMapping("/identification/{identificationId}")
    public OcrExtractionDTO getByIdentificationId(@PathVariable Long identificationId) {
        return service.getByIdentificationId(identificationId);
    }
}