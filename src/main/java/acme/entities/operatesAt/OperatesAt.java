
package acme.entities.operatesAt;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.airlines.Airlines;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OperatesAt extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airlines			operator;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				location;

}
