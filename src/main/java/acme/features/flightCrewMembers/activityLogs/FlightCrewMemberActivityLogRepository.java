
package acme.features.flightCrewMembers.activityLogs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select a from ActivityLog a where a.flightAssignment.flightCrewMember.id = :id")
	Collection<ActivityLog> findAllActivityLogsOfCrewMember(final int id);

	@Query("select a from ActivityLog a where a.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("select a from FlightAssignment a where a.id = :id")
	FlightAssignment findAssignmentById(int id);

	@Query("select a from ActivityLog a where a.flightAssignment.id = :id")
	Collection<ActivityLog> findAllActivityLogsOfAssignment(final int id);

}
