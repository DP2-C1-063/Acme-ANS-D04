
package acme.entities.customer;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$")
	//ESTE ES UNIQUE
	private String				identifier;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phone;

	@Mandatory
	@ValidString(max = 255)
	private String				address;

	@Mandatory
	@ValidString(max = 50)
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	private String				country;

	@Optional
	@ValidNumber(max = 500000)
	private Integer				points;

}
