// package pfe.backend.motDePasse.service;

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

    public MotDePasse save(MotDePasseDTO dto) {
        // 🔥 récupérer l'utilisateur via deviceId
        Identification identification = identificationRepository
                .findByDeviceId(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        String hashed = passwordEncoder.encode(dto.getMotDePasse());

        MotDePasse entity = MotDePasse.builder()
                .motDePasse(hashed)
                .identification(identification)
                .build();

        return repository.save(entity);
    }
    public MotDePasse getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "MotDePasse introuvable avec l'id : " + id));
    }
}