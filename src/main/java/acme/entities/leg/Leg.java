
package acme.entities.leg;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidFlightNumber;
import acme.constraints.ValidLeg;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidLeg
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidFlightNumber
	@Automapped
	private String				number;

	@Mandatory
	@ValidMoment(past = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment(past = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private Status				status;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				published;

	// Derived attributes -----------------------------------------------------


	@Transient
	public double getDuration() {
		double durationInHours = 0.;

		if (!(this.getScheduledArrival() == null || this.getScheduledDeparture() == null)) {
			long durationInMs = MomentHelper.computeDuration(this.scheduledDeparture, this.scheduledArrival).getSeconds();
			durationInHours = durationInMs / (60. * 60.);
		}
		return durationInHours;
	}

	@Transient
	public String getFlightNumber() {
		String IATACode = this.getFlight().getManager().getAirline().getIATACode();
		String number = this.getNumber();
		return IATACode + number;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

}
