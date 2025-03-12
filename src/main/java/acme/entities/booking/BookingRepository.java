
package acme.entities.booking;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface BookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :LocatorCode")
	Booking findBookingByLocatorCode(String LocatorCode);
}
