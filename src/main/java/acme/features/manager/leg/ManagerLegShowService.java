
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
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
		super.getResponse().setAuthorised(true);
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
		int airlineId;
		SelectChoices availableAircrafts;
		SelectChoices arrivalAirports;
		SelectChoices departureAirports;
		SelectChoices statuses;

		airlineId = leg.getFlight().getManager().getAirline().getId();
		aircrafts = this.repository.findAllAircraftsByAirlineId(airlineId);

		airports = this.repository.findAllAirports(); // findAllAirportsByOperatorId(int operatorId)

		statuses = SelectChoices.from(Status.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "duration", "departureAirport", "arrivalAirport");
		dataset.put("masterId", leg.getFlight().getId());
		dataset.put("published", leg.getFlight().isPublished());
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
		dataset.put("readonly", leg.isPublished());

		super.getResponse().addData(dataset);
	}
}
