package pfe.backend.adresse.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pfe.backend.adresse.model.Adresse;
import pfe.backend.identification.model.Identification;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    Optional<Adresse> findByIdentification(Identification identification);

}
