
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.flight.Indication;
import acme.entities.leg.Leg;
import acme.realms.manager.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {
	// Internal state

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		Manager manager;

		if (super.getRequest().getMethod().equals("POST")) {
			masterId = super.getRequest().getData("id", int.class);
			flight = this.repository.findFlightById(masterId);
			manager = flight == null ? null : flight.getManager();

			status = flight != null && !flight.isPublished() && super.getRequest().getPrincipal().hasRealm(manager);
		} else
			status = false;

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
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "indication", "cost", //
			"description", "scheduledDeparture", "scheduledArrival", "originCity", //
			"destinationCity", "numberOfLayovers");
	}

	@Override
	public void validate(final Flight flight) {
		{
			boolean allLegsArePublished;
			int masterId;
			Collection<Leg> legs;

			masterId = super.getRequest().getData("id", int.class);
			legs = this.repository.findLegsByFlightId(masterId);
			allLegsArePublished = true;
			for (Leg l : legs)
				if (!l.isPublished()) {
					allLegsArePublished = false;
					break;
				}

			super.state(allLegsArePublished, "*", "acme.validation.flight.legs-published.message");
		}
		{
			boolean atLeastOneLeg;
			int masterId;
			Collection<Leg> legs;

			masterId = super.getRequest().getData("id", int.class);
			legs = this.repository.findLegsByFlightId(masterId);
			atLeastOneLeg = legs != null ? legs.size() >= 1 : false;

			super.state(atLeastOneLeg, "*", "acme.validation.flight.no-legs.message");
		}
	}

	@Override
	public void perform(final Flight flight) {
		flight.setPublished(true);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		SelectChoices indications;
		indications = SelectChoices.from(Indication.class, flight.getIndication());

		dataset = super.unbindObject(flight, "tag", "indication", "cost", //
			"description", "scheduledDeparture", "scheduledArrival", "originCity", //
			"destinationCity", "numberOfLayovers", "published");
		dataset.put("indications", indications);
		dataset.put("indication", indications.getSelected());

		super.getResponse().addData(dataset);
	}
}
