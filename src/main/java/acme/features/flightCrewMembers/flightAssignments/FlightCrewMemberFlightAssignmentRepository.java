
package acme.features.flightCrewMembers.flightAssignments;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.AvailabilityStatus;
import acme.realms.flightCrewMembers.FlightCrewMembers;

@Repository
public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.flightCrewMember.id = :memberId and f.leg.scheduledArrival > :moment ")
	Collection<FlightAssignment> findAllPlannedAssignmentsOfCrewMember(int memberId, Date moment);

	@Query("select f from FlightAssignment f where f.flightCrewMember.id = :memberId and f.leg.scheduledArrival <= :moment")
	Collection<FlightAssignment> findAllCompletedAssignmentsOfCrewMember(int memberId, Date moment);

	@Query("select f from FlightAssignment f where f.id = :id")
	FlightAssignment findAssignmentById(int id);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT fcm FROM FlightCrewMembers fcm WHERE fcm.availabilityStatus = :status ")
	Collection<FlightCrewMembers> findAllCrewMembersAvailable(AvailabilityStatus status);

	@Query("Select fcm from FlightCrewMembers fcm where fcm.id = :id")
	FlightCrewMembers findCrewMemberById(int id);

	@Query("Select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("Select fcm from FlightCrewMembers fcm where fcm.employeeCode = :code")
	FlightCrewMembers findCrewMemberByCode(String code);

	@Query("Select l from ActivityLog l where l.flightAssignment.id = :id")
	List<ActivityLog> findLogsByAssignmentId(int id);

}
