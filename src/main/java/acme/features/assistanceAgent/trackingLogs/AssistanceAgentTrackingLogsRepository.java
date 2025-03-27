
package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogsRepository extends AbstractRepository {

	@Query("select tl from TrackingLog tl where tl.assistanceAgent.id = :Id ")
	Collection<TrackingLog> getAllTrackingLogsByAssistanceAgent(int Id);

}
