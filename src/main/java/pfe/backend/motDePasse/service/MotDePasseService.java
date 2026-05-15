package pfe.backend.motDePasse.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;
import pfe.backend.motDePasse.dto.MotDePasseDTO;
import pfe.backend.motDePasse.model.MotDePasse;
import pfe.backend.motDePasse.repository.MotDePasseRepository;

@Service
@RequiredArgsConstructor
public class MotDePasseService {

    private final MotDePasseRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final IdentificationRepository identificationRepository;

    public MotDePasse createOrUpdate(MotDePasseDTO dto) {

        // 🔥 récupérer l'utilisateur via deviceId
        Identification identification = identificationRepository
                .findByDeviceId(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // 🔥 vérifier si un mot de passe existe déjà
        MotDePasse entity = repository
                .findByIdentification(identification)
                .orElse(new MotDePasse());

        // 🔥 hash du mot de passe
        String hashed = passwordEncoder.encode(dto.getMotDePasse());

        // 🔥 update des champs
        entity.setMotDePasse(hashed);

        // 🔥 liaison avec identification
        entity.setIdentification(identification);

        return repository.save(entity);
    }

    public MotDePasse getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "MotDePasse introuvable avec l'id : " + id));
    }
}