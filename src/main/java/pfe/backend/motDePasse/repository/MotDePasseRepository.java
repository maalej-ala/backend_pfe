// package pfe.backend.motDePasse.repository;

package pfe.backend.motDePasse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.backend.motDePasse.model.MotDePasse;

@Repository
public interface MotDePasseRepository extends JpaRepository<MotDePasse, Long> {
}