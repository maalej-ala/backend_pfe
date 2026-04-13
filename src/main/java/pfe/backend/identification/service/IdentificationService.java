package pfe.backend.identification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pfe.backend.identification.dto.IdentificationRequest;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;

@Service
@RequiredArgsConstructor
public class IdentificationService {

    private final IdentificationRepository repository;

    public Identification saveIdentification(IdentificationRequest request) {
        // Vérifier si l'email existe déjà
        if (repository.findByDeviceId(request.getDeviceId()).isPresent()) {
            throw new RuntimeException("device id existe deja");
        }

        Identification identification = Identification.builder()
                .civilite(request.getCivilite())
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .dateNaissance(request.getDateNaissance())
                .accepteMentions(request.isAccepteMentions())
                .deviceId(request.getDeviceId())  // 🔥 sauvegarder le deviceId
                .build();

        return repository.save(identification);
    }
}