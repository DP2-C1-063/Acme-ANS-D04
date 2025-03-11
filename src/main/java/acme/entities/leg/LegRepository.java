
package acme.entities.leg;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flightNumber = :flightNumber")
	Leg findLegByFlightNumber(String flightNumber);

	@Query("select l from Leg l where l.flight = :flight order by l.scheduledDeparture")
	List<Leg> findLegsByFlight(Flight flight);

}
