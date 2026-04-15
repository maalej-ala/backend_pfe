package pfe.backend.verificationIdentite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.backend.verificationIdentite.model.OcrResult;

@Repository
public interface OcrResultRepository extends JpaRepository<OcrResult, Long> {
}
