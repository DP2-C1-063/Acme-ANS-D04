
package acme.features.administrator.airlines;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airlines;

@Repository
public interface AdministratorAirlinesRepository extends AbstractRepository {

	@Query("select a from Airlines a")
	Collection<Airlines> findAllAirlines();

	@Query("select a from Airlines a where a.id = :id")
	Airlines findAirlineById(int id);

}
