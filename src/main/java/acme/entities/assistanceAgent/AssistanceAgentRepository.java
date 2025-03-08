
package acme.entities.assistanceAgent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("select a from AssistanceAgent a where a.employeeCode = :employeeCode")
	AssistanceAgent findByEmployeeCode(String employeeCode);

}
