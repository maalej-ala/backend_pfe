package pfe.backend.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.backend.admin.model.DemandeCompte;
import pfe.backend.admin.model.StatutDemande;
import pfe.backend.verificationIdentite.model.VerificationIdentite;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeCompteRepository extends JpaRepository<DemandeCompte, Long> {
    Optional<DemandeCompte> findByVerificationIdentite(VerificationIdentite verificationIdentite);
    boolean existsByVerificationIdentite(VerificationIdentite verificationIdentite);
    List<DemandeCompte> findByStatut(StatutDemande statut);
}
