
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
public class ManagerLegShowService extends AbstractGuiService<Manager, Leg> {

	// Internal state

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
		if (leg != null) {
			flight = leg.getFlight();
			manager = flight.getManager();
			status = super.getRequest().getPrincipal().hasRealm(manager);
		} else
			status = false;

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

		dataset = super.unbindObject(leg, "flightNumber", "number", "scheduledDeparture", "scheduledArrival", "status", //
			"duration", "departureAirport", "arrivalAirport", "published");
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
