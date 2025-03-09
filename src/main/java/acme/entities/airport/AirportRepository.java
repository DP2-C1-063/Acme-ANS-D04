
package acme.entities.airport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirportRepository extends AbstractRepository {

	@Query("select a from Airport a where a.IATACode = :IATACode")
	Airport findAirportByIATACode(String IATACode);

}
