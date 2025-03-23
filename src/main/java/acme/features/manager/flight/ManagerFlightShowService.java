
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.flight.Indication;
import acme.realms.manager.Manager;

@GuiService
public class ManagerFlightShowService extends AbstractGuiService<Manager, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		SelectChoices choices;
		Dataset dataset;
		choices = SelectChoices.from(Indication.class, flight.getIndication());

		dataset = super.unbindObject(flight, "tag", "indication", "cost", //
			"published", "description", "scheduledDeparture", "scheduledArrival", "originCity", //
			"destinationCity", "numberOfLayovers");
		dataset.put("confirmation", false);
		dataset.put("readonly", true);
		dataset.put("operationalScopes", choices);
		super.getResponse().addData(dataset);
	}

}
