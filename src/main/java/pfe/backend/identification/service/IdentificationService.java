
package pfe.backend.identification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pfe.backend.identification.dto.IdentificationRequest;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.repository.VerificationIdentiteRepository;

@Service
@RequiredArgsConstructor
public class IdentificationService {

    private final IdentificationRepository repository;
    private final VerificationIdentiteRepository verificationRepository;

    @Transactional
    public Identification saveOrUpdateIdentification(IdentificationRequest request) {

        if (request.getDeviceId() == null || request.getDeviceId().isEmpty()) {
            throw new RuntimeException("deviceId obligatoire");
        }

        // 🔹 1. Trouver Identification existante (créée par OCR)
        Identification identification = repository.findByDeviceId(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException(
                        "Utilisateur introuvable. Veuillez d'abord passer par l'étape OCR."));

        // 🔹 2. Mettre à jour avec les données saisies par l'utilisateur
        if (isPresent(request.getCivilite())) {
            identification.setCivilite(request.getCivilite());
        }
        if (isPresent(request.getNom())) {
            identification.setNom(request.getNom());
        }
        if (isPresent(request.getPrenom())) {
            identification.setPrenom(request.getPrenom());
        }
        if (request.getDateNaissance() != null) {
            identification.setDateNaissance(request.getDateNaissance());
        }
        if (isPresent(request.getEmail())) {
            identification.setEmail(request.getEmail());
        }
        if (isPresent(request.getTelephone())) {
            identification.setTelephone(request.getTelephone());
        }
        if (request.getAccepteMentions() != null) {
            identification.setAccepteMentions(Boolean.TRUE.equals(request.getAccepteMentions()));
        }

        // 🔹 2.5. Mettre à jour CIN et date expiration si fournis
        String cinValue = request.getCin() ; // compatibilité
        if (isPresent(cinValue)) {
            identification.setCin(cinValue);
        }
        if (request.getDateExpiration() != null) {
            identification.setDateExpiration(request.getDateExpiration());
        }

        identification = repository.save(identification);

 
        return identification;
    }

    public Identification getByDeviceId(String deviceId) {

        return repository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException(
                        "Identification introuvable pour deviceId: " + deviceId
                ));
    }

    private boolean isPresent(String s) {
        return s != null && !s.isBlank();
    }
}