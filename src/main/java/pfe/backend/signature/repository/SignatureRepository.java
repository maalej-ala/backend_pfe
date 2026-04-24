package pfe.backend.signature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.backend.identification.model.Identification;
import pfe.backend.signature.model.Signature;

import java.util.Optional;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {
    Optional<Signature> findByIdentification(Identification identification);
}
