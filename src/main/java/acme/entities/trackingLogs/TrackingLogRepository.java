
package acme.entities.trackingLogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = : id order by t.lastUpdateMoment desc")
	List<TrackingLog> getLastTrackingRepositoryByClaim(Integer id);

}
