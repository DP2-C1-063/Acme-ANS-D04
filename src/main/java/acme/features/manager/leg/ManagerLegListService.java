
package acme.features.manager.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Leg> legs;
		int flightId;

		flightId = super.getRequest().getData("id", int.class);

		legs = this.repository.findAllLegsByFlightId(flightId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "duration", "departureAirport", "arrivalAirport", "aircraft", "flight");

		super.getResponse().addData(dataset);
	}

}
