
package acme.features.customers.passengers;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.customer.id=:customerId")
	Collection<Passenger> findAllPassengersOfCustomer(int customerId);

	@Query("select p from Passenger p where p.id=:passengerId")
	Passenger findPassengerById(int passengerId);

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.id = :bookingId")
	Collection<Passenger> findAllPassengersByBooking(int bookingId);

	@Query("select b from Booking b where b.id=:bookingId")
	Booking findBookingById(int bookingId);

}
