package pfe.backend.identification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.backend.identification.model.Identification;
import java.util.Optional;

public interface IdentificationRepository extends JpaRepository<Identification, Long> {
	Optional<Identification> findByDeviceId(String deviceId);}