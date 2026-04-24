package pfe.backend.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pfe.backend.admin.dto.*;
import pfe.backend.admin.model.DemandeCompte;
import pfe.backend.admin.model.OcrDiscrepancy;
import pfe.backend.admin.model.StatutDemande;
import pfe.backend.admin.repository.DemandeCompteRepository;
import pfe.backend.identification.model.Identification;
import pfe.backend.verificationIdentite.model.VerificationIdentite;
import pfe.backend.verificationIdentite.repository.VerificationIdentiteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminDemandeService {

    private final DemandeCompteRepository demandeCompteRepository;
    private final VerificationIdentiteRepository verificationIdentiteRepository;

    // ── GET toutes les demandes ───────────────────────────────
    public List<AdminDossierDTO> getAllDemandes() {
        return demandeCompteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

//     // ── GET une demande par id ────────────────────────────────
//     public AdminDossierDTO getDemandeById(Long id) {
//         DemandeCompte demande = demandeCompteRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Demande introuvable : " + id));
//         return toDTO(demande);
//     }

  

    // ── Mapper entité → DTO ───────────────────────────────────
    private AdminDossierDTO toDTO(DemandeCompte d) {

        VerificationIdentite v = d.getVerificationIdentite();
        Identification i = v.getIdentification();

        return AdminDossierDTO.builder()
                .id(d.getId())
                .nom(i.getNom())
                .prenom(i.getPrenom())
                .telephone(i.getTelephone())
                .email(i.getEmail())
                .cin(v.getCin())
                .scoreSimilarite(d.getScoreSimilarite())
                .statut(d.getStatut().name())
                .build();
    }

//     private IdentificationDTO toIdentificationDTO(Identification i) {
//         return IdentificationDTO.builder()
//                 .civilite(i.getCivilite())
//                 .accepteMentions(i.isAccepteMentions())
//                 .nom(i.getNom())
//                 .prenom(i.getPrenom())
//                 .telephone(i.getTelephone())
//                 .email(i.getEmail())
//                 .dateNaissance(i.getDateNaissance() != null ? i.getDateNaissance().toString() : null)
//                 .build();
//     }

//     private VerificationIdentiteDTO toVerificationDTO(VerificationIdentite v, Identification i) {
//         return VerificationIdentiteDTO.builder()
//                 .id(v.getId())
//                 .cin(v.getCin())
//                 .dateDelivrance(v.getDateDelivrance() != null ? v.getDateDelivrance().toString() : null)
//                 .estClientAutreBanque(v.isEstClientAutreBanque())
//                 .photoCinPath(v.getPhotoCinPath())
//                 .photoVisageCinPath(v.getPhotoVisageCinPath())
//                 .photoVisageLivePath(v.getPhotoVisageLivePath())
//                 .identification(toIdentificationDTO(i))
//                 .build();
//     }
}
