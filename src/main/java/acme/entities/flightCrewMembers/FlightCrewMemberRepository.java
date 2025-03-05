
package acme.entities.flightCrewMembers;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("select m from FlightCrewMembers m where m.employeeCode = :employeeCode")
	FlightCrewMembers findMemberByEmployeeCode(String employeeCode);

}
