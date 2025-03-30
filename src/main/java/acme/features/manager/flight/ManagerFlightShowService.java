
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
		boolean status;
		int flightId;
		Flight flight;
		Manager manager;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId);
		manager = flight == null ? null : flight.getManager();
		status = flight != null && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
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
		Dataset dataset;
		boolean published;
		SelectChoices indications;
		indications = SelectChoices.from(Indication.class, flight.getIndication());

		published = flight.isPublished();

		dataset = super.unbindObject(flight, "tag", "indication", "cost", //
			"published", "description", "scheduledDeparture", "scheduledArrival", "originCity", //
			"destinationCity", "numberOfLayovers");

		if (published)
			dataset.put("readonly", true);
		else
			dataset.put("readonly", false);
		dataset.put("indications", indications);
		dataset.put("indication", indications.getSelected());
		dataset.put("masterId", flight.getId());

		super.getResponse().addData(dataset);
	}

}
