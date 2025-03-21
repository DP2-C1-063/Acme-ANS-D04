
package acme.features.flightCrewMembers.flightAssignments;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Status;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.flightCrewMember.id = :memberId and f.leg.scheduledDeparture > :moment ")
	Collection<FlightAssignment> findAllPlannedAssignmentsOfCrewMember(int memberId, Date moment);

	@Query("select f from FlightAssignment f where f.flightCrewMember.id = :memberId and f.leg.status = :status")
	Collection<FlightAssignment> findAllCompletedAssignmentsOfCrewMember(int memberId, Status status);

	@Query("select f from FlightAssignment f where f.id = :id")
	FlightAssignment findAssignmentById(int id);

}
