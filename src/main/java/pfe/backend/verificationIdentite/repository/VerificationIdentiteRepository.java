package pfe.backend.verificationIdentite.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pfe.backend.identification.model.Identification;
import pfe.backend.verificationIdentite.model.VerificationIdentite;

@Repository
public interface VerificationIdentiteRepository
        extends JpaRepository<VerificationIdentite, Long> {
                Optional<VerificationIdentite> findByIdentification(Identification identification);

Optional<VerificationIdentite> findByIdentificationDeviceId(String deviceId);

  @Query("""
        SELECT v
        FROM VerificationIdentite v
        JOIN v.identification i
        WHERE i.deviceId = :deviceId
    """)
    Optional<VerificationIdentite> findByDeviceId(String deviceId);

}