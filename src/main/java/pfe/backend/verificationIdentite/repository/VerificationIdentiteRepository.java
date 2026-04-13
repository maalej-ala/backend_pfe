package pfe.backend.verificationIdentite.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pfe.backend.identification.model.Identification;
import pfe.backend.verificationIdentite.model.VerificationIdentite;

@Repository
public interface VerificationIdentiteRepository
        extends JpaRepository<VerificationIdentite, Long> {
                Optional<VerificationIdentite> findByIdentification(Identification identification);

}