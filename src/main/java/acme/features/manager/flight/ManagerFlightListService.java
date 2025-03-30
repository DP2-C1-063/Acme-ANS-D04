
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.manager.Manager;

@GuiService
public class ManagerFlightListService extends AbstractGuiService<Manager, Flight> {
	// Internal state

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Flight> flights;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		flights = this.repository.findAllFlightsByManagerId(managerId);

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "cost", //
			"scheduledDeparture", "originCity", //
			"destinationCity");

		super.addPayload(dataset, flight, "indication", "published", "description", "numberOfLayovers", "scheduledArrival");

		super.getResponse().addData(dataset);
	}

}
