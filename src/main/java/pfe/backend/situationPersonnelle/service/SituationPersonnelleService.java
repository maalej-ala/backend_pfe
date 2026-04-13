package pfe.backend.situationPersonnelle.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pfe.backend.identification.model.Identification;
import pfe.backend.identification.repository.IdentificationRepository;
import pfe.backend.situationPersonnelle.dto.SituationPersonnelleDTO;
import pfe.backend.situationPersonnelle.model.SituationPersonnelle;
import pfe.backend.situationPersonnelle.repository.SituationPersonnelleRepository;

@Service
@RequiredArgsConstructor
public class SituationPersonnelleService {

    private final SituationPersonnelleRepository repository;
    private final IdentificationRepository identificationRepository;

    public SituationPersonnelle save(SituationPersonnelleDTO dto) {
        // 🔥 récupérer l'utilisateur via deviceId
        Identification identification = identificationRepository
                .findByDeviceId(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        SituationPersonnelle entity = SituationPersonnelle.builder()
                .nationalite(dto.getNationalite())
                .statutCivil(dto.getStatutCivil())
                .nbEnfants(dto.getNbEnfants())
                .identification(identification)
                .build();

        return repository.save(entity);
    }

    public SituationPersonnelle getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                    "SituationPersonnelle introuvable avec l'id : " + id));
    }
}