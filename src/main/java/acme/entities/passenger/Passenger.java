
package acme.entities.passenger;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.booking.Booking;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 255)
	private String				name;

	@Mandatory
	@ValidEmail
	private String				email;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	private String				passport;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.DATE)
	private Date				birth;

	@Optional
	@ValidString(max = 50)
	private String				needs;

	@Mandatory
	@Valid
	@OneToMany
	@JoinColumn(name = "booking_id")
	private Booking				booking;

}
