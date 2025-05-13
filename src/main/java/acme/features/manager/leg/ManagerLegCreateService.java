
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
public class ManagerLegCreateService extends AbstractGuiService<Manager, Leg> {

	// Internal state
	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;

		masterId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(masterId);

		// Avoiding POST hacking

		Integer departureAirportId = super.getRequest().getData("departureAirport", int.class);
		Airport departureAirport = this.repository.findAirportById(departureAirportId);
		boolean departureAirportExists = !(departureAirport == null && departureAirportId != 0);
		Integer arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		Airport arrivalAirport = this.repository.findAirportById(arrivalAirportId);
		boolean arrivalAirportExists = !(arrivalAirport == null && arrivalAirportId != 0);
		Integer aircraftId = super.getRequest().getData("aircraft", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId);
		boolean aircraftExists = !(aircraft == null && aircraftId != 0);

		status = flight != null && !flight.isPublished() && super.getRequest().getPrincipal().hasRealm(flight.getManager()) && departureAirportExists && arrivalAirportExists && aircraftExists;

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		Leg leg;
		Flight flight;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(masterId);

		// Setting the leg
		leg = new Leg();
		leg.setStatus(Status.ON_TIME);
		leg.setPublished(false);
		leg.setFlight(flight);

		super.getBuffer().addData(leg);
	}
	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "number", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft");

	}
	@Override
	public void validate(final Leg leg) {
		;
	}
	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;
		dataset = super.unbindObject(leg, "number", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft");

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

		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
