
package acme.entities.flight;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository {

	@Query("select f from Flight f where f = :flight")
	Flight findFlight(Flight flight);

}
