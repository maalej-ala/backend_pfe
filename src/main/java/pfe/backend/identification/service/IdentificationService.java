//package pfe.backend.identification.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import pfe.backend.identification.dto.IdentificationRequest;
//import pfe.backend.identification.model.Identification;
//import pfe.backend.identification.repository.IdentificationRepository;
//import pfe.backend.verificationIdentite.model.VerificationIdentite;
//import pfe.backend.verificationIdentite.repository.VerificationIdentiteRepository;
//
//@Service
//@RequiredArgsConstructor
//public class IdentificationService {
//
//    private final IdentificationRepository repository;
//    private final VerificationIdentiteRepository verificationRepository;
//
//
//// public Identification saveOrUpdateIdentification(IdentificationRequest request) {
//
////     if (request.getDeviceId() == null || request.getDeviceId().isEmpty()) {
////         throw new RuntimeException("deviceId obligatoire");
////     }
//
////     Identification identification = repository.findByDeviceId(request.getDeviceId())
////             .orElse(new Identification()); // 🔥 créer si n'existe pas
//
////     // 🔥 UPDATE ou CREATE (même code)
////     identification.setCivilite(request.getCivilite());
////     identification.setNom(request.getNom());
////     identification.setPrenom(request.getPrenom());
////     // identification.setSexe(request.getSexe());
////     identification.setDateNaissance(request.getDateNaissance());
////     identification.setEmail(request.getEmail());
////     identification.setTelephone(request.getTelephone());
////     identification.setAccepteMentions(Boolean.TRUE.equals(request.getAccepteMentions()));
////     identification.setDeviceId(request.getDeviceId());
//
////     return repository.save(identification);
//// }
//
//public Identification saveOrUpdateIdentification(IdentificationRequest request) {
//
//    if (request.getDeviceId() == null || request.getDeviceId().isEmpty()) {
//        throw new RuntimeException("deviceId obligatoire");
//    }
//
//    // 🔹 1. Find or create Identification
//    Identification identification = repository.findByDeviceId(request.getDeviceId())
//            .orElse(new Identification());
//
//    // 🔹 2. Update Identification
//    identification.setCivilite(request.getCivilite());
//    identification.setNom(request.getNom());
//    identification.setPrenom(request.getPrenom());
//    identification.setDateNaissance(request.getDateNaissance());
//    identification.setEmail(request.getEmail());
//    identification.setTelephone(request.getTelephone());
//    identification.setAccepteMentions(Boolean.TRUE.equals(request.getAccepteMentions()));
//    identification.setDeviceId(request.getDeviceId());
//
//    identification = repository.save(identification);
//
//    // 🔹 3. Handle VerificationIdentite (OCR data)
//    if (request.getNumero() != null || request.getDateExpiration() != null) {
//
//    	VerificationIdentiteRepository verification = verificationRepository
//                .findByIdentification(identification)
//                .orElse(new VerificationIdentite());
//
//        // 🔥 Mapping OCR → DB
//        if (request.getNumero() != null) {
//            verification.setCin(request.getNumero());
//        }
//
//        if (request.getDateExpiration() != null) {
//            verification.setDateDelivrance(request.getDateExpiration()); // ⚠️ your mapping
//        }
//
//        verification.setIdentification(identification);
//
//        // ⚠️ required fields (avoid crash)
//        if (verification.getEstClientAutreBanque() == false) {
//            verification.setEstClientAutreBanque(false);
//        }
//
//        verificationRepository.save(verification);
//    }
//
//    return identification;
//}
//
//public Identification getByDeviceId(String deviceId) {
//
//    return repository.findByDeviceId(deviceId)
//            .orElseThrow(() -> new RuntimeException(
//                    "Identification introuvable pour deviceId: " + deviceId
//            ));
//}
//}



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

        // 🔹 1. Find or create Identification
        Identification identification = repository.findByDeviceId(request.getDeviceId())
                .orElse(new Identification());

        // 🔹 2. Update Identification
        identification.setCivilite(request.getCivilite());
        identification.setNom(request.getNom());
        identification.setPrenom(request.getPrenom());
        identification.setDateNaissance(request.getDateNaissance());
        identification.setEmail(request.getEmail());
        identification.setTelephone(request.getTelephone());
        identification.setAccepteMentions(Boolean.TRUE.equals(request.getAccepteMentions()));
        identification.setDeviceId(request.getDeviceId());

        identification = repository.save(identification);

        // 🔹 3. Handle VerificationIdentite (OCR data)
        if (request.getNumero() != null || request.getDateExpiration() != null) {

            // ✅ CORRECT TYPE (entity)
            VerificationIdentite verification = verificationRepository
                    .findByIdentification(identification)
                    .orElse(new VerificationIdentite());

            // 🔥 Mapping OCR → DB
            if (request.getNumero() != null && !request.getNumero().isBlank()) {
                verification.setCin(request.getNumero());
            }

            if (request.getDateExpiration() != null) {
                // 👉 use correct field (NOT dateDelivrance)
                verification.setDateExpiration(request.getDateExpiration());
            }

            // 🔗 Link to Identification
            verification.setIdentification(identification);

            // ⚠️ Safe default (if null)
            if (verification.getEstClientAutreBanque() == null) {
                verification.setEstClientAutreBanque(false);
            }

            verificationRepository.save(verification);
        }

        return identification;
    }

    public Identification getByDeviceId(String deviceId) {

        return repository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException(
                        "Identification introuvable pour deviceId: " + deviceId
                ));
    }
}