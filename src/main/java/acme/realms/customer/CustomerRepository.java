
package acme.realms.customer;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface CustomerRepository extends AbstractRepository {

	@Query("select c from Customer c where c.identifier = :Identifier")
	Customer findCustomerByIdentifier(String Identifier);

}
