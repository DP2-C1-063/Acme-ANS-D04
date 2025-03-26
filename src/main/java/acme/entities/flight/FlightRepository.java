
package acme.entities.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.booking.id = :bookingId")
	Collection<Flight> findAllFlightsByBooking(int bookingId);

}
