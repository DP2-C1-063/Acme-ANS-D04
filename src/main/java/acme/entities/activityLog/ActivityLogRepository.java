
package acme.entities.activityLog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface ActivityLogRepository extends AbstractRepository {

	@Query("select a.flightAssignment.leg from ActivityLog a where a.flightAssignment.id = :id")
	Leg findLegOfLogThroughAssignment(int id);
}
