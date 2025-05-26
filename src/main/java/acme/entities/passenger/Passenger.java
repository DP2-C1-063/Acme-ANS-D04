
package acme.entities.passenger;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidPassport;
import acme.realms.customer.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(indexes = {
	@Index(columnList = "customer_id")
})
public class Passenger extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				name;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				email;

	@Mandatory
	@ValidPassport
	@Automapped
	private String				passport;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Automapped
	private Date				birth;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				needs;

	@Mandatory
	@Automapped
	private boolean				draftMode			= true;

	@Mandatory
	@Valid
	@ManyToOne
	private Customer			customer;

}
