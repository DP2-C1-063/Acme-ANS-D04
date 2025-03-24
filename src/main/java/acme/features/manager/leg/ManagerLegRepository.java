
package acme.features.manager.leg;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.flight.id = :masterId order by l.scheduledDeparture")
	List<Leg> findAllLegsByMasterId(int masterId);

	@Query("select f from Flight f where f.id = :masterId")
	Flight findFlightById(int masterId);

}
