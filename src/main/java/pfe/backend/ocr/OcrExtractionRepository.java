package pfe.backend.ocr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pfe.backend.identification.model.Identification;

import java.util.Optional;

@Repository
public interface OcrExtractionRepository extends JpaRepository<OcrExtraction, Long> {
    Optional<OcrExtraction> findByIdentification(Identification identification);
}