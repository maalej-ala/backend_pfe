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

    public SituationProfessionnelle createOrUpdate(SituationProfessionnelleDTO dto) {

        // 🔥 récupérer l'utilisateur via deviceId
        Identification identification = identificationRepository
                .findByDeviceId(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // 🔥 vérifier si une situation professionnelle existe déjà
        SituationProfessionnelle entity = repository
                .findByIdentification(identification)
                .orElse(new SituationProfessionnelle());

        // 🔥 update des champs
        entity.setCategorieSocioPro(dto.getCategorieSocioPro());
        entity.setRevenu(dto.getRevenu());
        entity.setNatureActivite(dto.getNatureActivite());
        entity.setSecteurActivite(dto.getSecteurActivite());

        // 🔥 liaison avec identification
        entity.setIdentification(identification);

        return repository.save(entity);
    }

    public SituationProfessionnelle getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "SituationProfessionnelle introuvable avec l'id : " + id));
    }
}