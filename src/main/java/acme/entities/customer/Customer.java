
package acme.entities.customer;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidCustomer;
import acme.constraints.ValidIdentifier;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidCustomer
public class Customer extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidIdentifier
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phone;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				address;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidNumber(max = 500000)
	@Automapped
	private Integer				points;

}
