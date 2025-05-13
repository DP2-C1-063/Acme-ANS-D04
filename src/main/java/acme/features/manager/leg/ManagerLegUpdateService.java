
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.Status;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegUpdateService extends AbstractGuiService<Manager, Leg> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		Flight flight;
		Manager manager;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);
		flight = leg.getFlight();
		manager = flight == null ? null : flight.getManager();

		// Avoiding POST hacking

		Integer departureAirportId = super.getRequest().getData("departureAirport", int.class);
		Airport departureAirport = this.repository.findAirportById(departureAirportId);
		boolean departureAirportExists = !(departureAirport == null && departureAirportId != 0);

		status = leg != null && flight != null && super.getRequest().getPrincipal().hasRealm(manager) && departureAirportExists;

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
		super.bindObject(leg, "number", "scheduledDeparture", "scheduledArrival", //
			"status", "departureAirport", "arrivalAirport", "aircraft");
	}

	@Override
	public void validate(final Leg leg) {
		{
			boolean isNotPublished;

			isNotPublished = !leg.isPublished();
			super.state(isNotPublished, "published", "acme.validation.published.message");
		}
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "number", "scheduledDeparture", "scheduledArrival", //
			"status", "departureAirport", "arrivalAirport", "aircraft", "published");

		// Status --------------------------------------------------------

		SelectChoices statuses;
		statuses = SelectChoices.from(Status.class, leg.getStatus());
		dataset.put("statuses", statuses);
		dataset.put("status", statuses.getSelected());

		// Airports ------------------------------------------------------

		SelectChoices arrivalAirports;
		SelectChoices departureAirports;
		Collection<Airport> airports;
		airports = this.repository.findAllAirports();
		arrivalAirports = SelectChoices.from(airports, "name", leg.getArrivalAirport());
		departureAirports = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		dataset.put("arrivalAirports", arrivalAirports);
		dataset.put("arrivalAirport", arrivalAirports.getSelected());
		dataset.put("departureAirports", departureAirports);
		dataset.put("departureAirport", departureAirports.getSelected());

		// Aircrafts ------------------------------------------------------

		SelectChoices availableAircrafts;
		Collection<Aircraft> aircrafts;

		aircrafts = this.repository.findAllAircrafts();
		availableAircrafts = SelectChoices.from(aircrafts, "model", leg.getAircraft());
		dataset.put("aircrafts", availableAircrafts);
		dataset.put("aircraft", availableAircrafts.getSelected());

		super.getResponse().addData(dataset);
	}

}
