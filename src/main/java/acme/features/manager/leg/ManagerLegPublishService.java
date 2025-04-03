
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.Status;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegPublishService extends AbstractGuiService<Manager, Leg> {
	// Internal state

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		Flight flight;
		Manager manager;
		Leg leg;
		int legId;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);
		flight = leg.getFlight();
		manager = flight == null ? null : flight.getManager();
		status = flight != null && leg != null && !leg.isPublished() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "number", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", //
			"arrivalAirport", "aircraft");
	}

	@Override
	public void validate(final Leg leg) {
		{
			boolean aircraftNotInUse;
			Collection<Leg> legsSameAircraft;
			int aircraftId;
			Date departure;
			Date arrival;

			aircraftId = leg.getAircraft().getId();
			legsSameAircraft = this.repository.findAllLegsByAircraftId(aircraftId);
			legsSameAircraft.remove(leg);
			departure = leg.getScheduledDeparture();
			arrival = leg.getScheduledArrival();
			aircraftNotInUse = true;
			if (departure != null && arrival != null)
				for (Leg l : legsSameAircraft) {
					Date startDate = l.getScheduledDeparture();
					Date endDate = l.getScheduledArrival();
					if (startDate != null && endDate != null) {
						boolean startsWithinInterval = MomentHelper.isAfterOrEqual(departure, startDate) && MomentHelper.isBeforeOrEqual(departure, endDate);
						boolean endsWithinInterval = MomentHelper.isAfterOrEqual(arrival, startDate) && MomentHelper.isBeforeOrEqual(arrival, endDate);
						boolean coversEntireInterval = MomentHelper.isBeforeOrEqual(departure, startDate) && MomentHelper.isAfterOrEqual(arrival, endDate);

						if (startsWithinInterval || endsWithinInterval || coversEntireInterval) {
							aircraftNotInUse = false;
							break;
						}
					}
				}

			super.state(aircraftNotInUse, "*", "acme.validation.leg.aircraft-in-use.message");
		}
	}

	@Override
	public void perform(final Leg leg) {
		leg.setPublished(true);
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		Collection<Aircraft> aircrafts;
		Collection<Airport> airports;
		SelectChoices availableAircrafts;
		SelectChoices arrivalAirports;
		SelectChoices departureAirports;
		SelectChoices statuses;

		aircrafts = this.repository.findAllAircrafts();
		airports = this.repository.findAllAirports();

		statuses = SelectChoices.from(Status.class, leg.getStatus());

		dataset = super.unbindObject(leg, "number", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", //
			"arrivalAirport", "aircraft", "published");

		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("statuses", statuses);
		dataset.put("status", statuses.getSelected());

		if (!aircrafts.isEmpty()) {
			availableAircrafts = SelectChoices.from(aircrafts, "model", leg.getAircraft());
			dataset.put("aircrafts", availableAircrafts);
		}
		if (!airports.isEmpty()) {
			arrivalAirports = SelectChoices.from(airports, "name", leg.getArrivalAirport());
			departureAirports = SelectChoices.from(airports, "name", leg.getDepartureAirport());
			dataset.put("departureAirports", departureAirports);
			dataset.put("arrivalAirports", arrivalAirports);
			dataset.put("departureAirport", departureAirports.getSelected());
			dataset.put("arrivalAirport", arrivalAirports.getSelected());
		}

		dataset.put("aircraft", leg.getAircraft().getId());

		super.getResponse().addData(dataset);
	}

}
