
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidBooking;
import acme.constraints.ValidLocatorCode;
import acme.entities.flight.Flight;
import acme.realms.customer.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidLocatorCode
	@Automapped
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Optional
	@ValidString(pattern = "^\\d{4}$")
	@Automapped
	private String				lastNibble;

	@Mandatory
	@Valid
	@ManyToOne
	private Customer			customer;

	@Mandatory
	@ManyToOne
	@Valid
	private Flight				flight;

	@Mandatory
	@Automapped
	private boolean				draftMode			= true;


	@Transient
	public Money getPrice() {
		Money flightCost = this.getFlight().getCost();
		BookingRepository bookingRepository = SpringHelper.getBean(BookingRepository.class);
		Integer numberOfPassengers = bookingRepository.getNumberPassengersOfBooking(this.getId());
		Money res = new Money();
		res.setCurrency(flightCost.getCurrency());
		res.setAmount(flightCost.getAmount() * numberOfPassengers);
		return res;
	}

}
