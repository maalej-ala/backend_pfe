package pfe.backend.situationProfessionnelle.service;

import lombok.RequiredArgsConstructor;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;
import pfe.backend.situationProfessionnelle.dto.SituationProfessionnelleDTO;
import pfe.backend.situationProfessionnelle.model.SituationProfessionnelle;
import pfe.backend.situationProfessionnelle.repository.SituationProfessionnelleRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SituationProfessionnelleService {

    private final SituationProfessionnelleRepository repository;
    private final IdentificationRepository identificationRepository;

    public SituationProfessionnelle save(SituationProfessionnelleDTO dto) {
        // 🔥 récupérer l'utilisateur via deviceId
        Identification identification = identificationRepository
                .findByDeviceId(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        SituationProfessionnelle entity = SituationProfessionnelle.builder()
                .categorieSocioPro(dto.getCategorieSocioPro())
                .revenu(dto.getRevenu())
                .natureActivite(dto.getNatureActivite())
                .secteurActivite(dto.getSecteurActivite())
                .identification(identification)
                .build();

        return repository.save(entity);
    }

    public SituationProfessionnelle getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "SituationProfessionnelle introuvable avec l'id : " + id));
    }
}