
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;
import acme.realms.manager.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlight
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Indication			indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		Date result = null;
		LegRepository legRepository;
		List<Leg> wrapper;

		legRepository = SpringHelper.getBean(LegRepository.class);
		wrapper = legRepository.findLegsByFlight(this);
		if (!wrapper.isEmpty() && wrapper != null)
			result = wrapper.getFirst().getScheduledDeparture();

		return result;
	}

	@Transient
	public Date getScheduledArrival() {
		Date result = null;
		LegRepository legRepository;
		List<Leg> wrapper;

		legRepository = SpringHelper.getBean(LegRepository.class);
		wrapper = legRepository.findLegsByFlight(this);
		if (!wrapper.isEmpty() && wrapper != null)
			result = wrapper.getLast().getScheduledArrival();

		return result;
	}

	@Transient
	public String getOriginCity() {
		String result = null;
		LegRepository legRepository;
		List<Leg> wrapper;

		legRepository = SpringHelper.getBean(LegRepository.class);
		wrapper = legRepository.findLegsByFlight(this);
		if (!wrapper.isEmpty() && wrapper != null)
			result = wrapper.getFirst().getDepartureAirport().getCity();

		return result;
	}

	@Transient
	public String getDestinationCity() {
		String result = null;
		LegRepository legRepository;
		List<Leg> wrapper;

		legRepository = SpringHelper.getBean(LegRepository.class);
		wrapper = legRepository.findLegsByFlight(this);
		if (!wrapper.isEmpty() && wrapper != null)
			result = wrapper.getLast().getArrivalAirport().getCity();

		return result;
	}

	@Transient
	public int getNumberOfLayovers() {
		int result = 0;
		LegRepository legRepository;
		List<Leg> wrapper;

		legRepository = SpringHelper.getBean(LegRepository.class);
		wrapper = legRepository.findLegsByFlight(this);
		if (!wrapper.isEmpty() && wrapper != null)
			result = wrapper.size() - 1;

		return result;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager manager;
}
