package pfe.backend.situationProfessionnelle.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pfe.backend.identification.model.Identification;
import pfe.backend.situationProfessionnelle.model.SituationProfessionnelle;

@Repository
public interface SituationProfessionnelleRepository
        extends JpaRepository<SituationProfessionnelle, Long> {
                Optional<SituationProfessionnelle> findByIdentification(Identification identification);

}