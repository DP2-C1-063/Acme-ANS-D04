
package acme.entities.booking;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface BookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :LocatorCode")
	Booking findBookingByLocatorCode(String LocatorCode);

	@Query("SELECT COUNT(br) FROM BookingRecord br WHERE br.booking.id = :bookingId")
	Integer getNumberPassengersOfBooking(Integer bookingId);
}
