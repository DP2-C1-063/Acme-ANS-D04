
package acme.entities.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT f.leg FROM FlightAssignment f WHERE f.flightCrewMember.id = :id AND ((:departure BETWEEN f.leg.scheduledDeparture AND f.leg.scheduledArrival) OR (:arrival BETWEEN f.leg.scheduledDeparture AND f.leg.scheduledArrival) OR (f.leg.scheduledDeparture BETWEEN :departure AND :arrival) OR (f.leg.scheduledArrival BETWEEN :departure AND :arrival)) and f.leg.id != :legId")
	Collection<Leg> findAllConcurrentLegs(int id, Date departure, Date arrival, int legId);

	@Query("select f From FlightAssignment f where f.duty = :duty and f.leg.id = :legId and f.flightCrewMember.id != :memberId")
	Collection<FlightAssignment> findDutyAlreadyInLeg(Duty duty, int legId, int memberId);
}
