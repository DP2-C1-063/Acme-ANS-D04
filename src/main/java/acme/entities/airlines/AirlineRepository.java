
package acme.entities.airlines;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("select a from Airlines a where a.IATACode = :IATACode")
	Airlines findAirlineByIATA(String IATACode);

}
