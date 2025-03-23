
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.manager.id = :managerId")
	Collection<Flight> findAllFlightsByManagerId(int managerId);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

}
