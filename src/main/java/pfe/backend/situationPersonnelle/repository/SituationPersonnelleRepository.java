package pfe.backend.situationPersonnelle.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pfe.backend.identification.model.Identification;
import pfe.backend.situationPersonnelle.model.SituationPersonnelle;

@Repository
public interface SituationPersonnelleRepository 
    extends JpaRepository<SituationPersonnelle, Long> {
        Optional<SituationPersonnelle> findByIdentification(Identification identification);

}