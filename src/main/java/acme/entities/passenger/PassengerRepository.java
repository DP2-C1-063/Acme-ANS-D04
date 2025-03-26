
package acme.entities.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface PassengerRepository extends AbstractRepository {

	@Query("SELECT p FROM Passenger p WHERE p.booking.id = :bookingId")
	Collection<Passenger> findAllPassengersByBooking(int bookingId);

}
