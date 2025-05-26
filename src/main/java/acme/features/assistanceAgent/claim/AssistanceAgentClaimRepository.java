
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.assistanceAgent.id = :Id ")
	Collection<Claim> findAllClaimsByAssistanceAgent(final int Id);

	@Query("select c from Claim c where c.id= :Id")
	Claim findClaim(final int Id);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("Select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select t from TrackingLog t where t.claim.id = :Id order by t.lastUpdateMoment desc")
	Collection<TrackingLog> getAllTrackingLogsByClaim(int Id);

}
