
package acme.entities.aircraft;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AircraftRepository extends AbstractRepository {

	@Query("SELECT a FROM Aircraft a WHERE a.registrationNumber = :registrationNumber")
	List<Aircraft> findByRegistrationNumber(String registrationNumber);
}
