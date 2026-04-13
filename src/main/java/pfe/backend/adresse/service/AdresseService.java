package pfe.backend.adresse.service;


import lombok.RequiredArgsConstructor;
import pfe.backend.adresse.dto.AdresseDTO;
import pfe.backend.adresse.model.Adresse;
import pfe.backend.adresse.repository.AdresseRepository;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdresseService {

    private final AdresseRepository adresseRepository;
    private final IdentificationRepository identificationRepository;

    public Adresse saveAdresse(AdresseDTO dto) {

        // 🔥 récupérer l'utilisateur via deviceId
        Identification identification = identificationRepository
                .findByDeviceId(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // 🔥 créer adresse liée
        Adresse adresse = Adresse.builder()
                .adresse(dto.getAdresse())
                .paysNom(dto.getPaysNom())
                .gouvernorat(dto.getGouvernorat())
                .codePostal(dto.getCodePostal())
                .identification(identification)
                .build();

        return adresseRepository.save(adresse);
    }

    public Adresse getAdresseById(Long id) {
        return adresseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adresse introuvable avec l'id : " + id));
    }
}