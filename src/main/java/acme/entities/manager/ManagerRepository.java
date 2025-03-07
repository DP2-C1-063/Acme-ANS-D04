
package acme.entities.manager;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository {

	@Query("select m from Manager m where m.identifierNumber = :identifierNumber")
	Manager findManagerByIdentifierNumber(String identifierNumber);

}
