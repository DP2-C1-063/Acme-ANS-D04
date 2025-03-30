
package acme.features.manager.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegListService extends AbstractGuiService<Manager, Leg> {
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
		status = flight != null && !flight.isPublished();

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Leg> legs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		legs = this.repository.findAllLegsByMasterId(masterId);
		super.getResponse().addGlobal("masterId", masterId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "duration", "departureAirport", "arrivalAirport", "aircraft", "flight");
		dataset.put("masterId", masterId);
		super.getResponse().addGlobal("masterId", masterId);

		super.getResponse().addData(dataset);
	}

}
