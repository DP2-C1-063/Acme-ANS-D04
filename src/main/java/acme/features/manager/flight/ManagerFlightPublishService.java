
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
		Collection<Leg> legs;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		manager = flight == null ? null : flight.getManager();
		legs = this.repository.findLegsByFlightId(masterId);
		// Falta comprobar que todos los legs estén publicados
		status = flight != null && !flight.isPublished() && super.getRequest().getPrincipal().hasRealm(manager) && legs.size() >= 1;

		super.getResponse().setAuthorised(status);
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
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "indication", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		;
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

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "description", "published");
		dataset.put("indications", indications);
		dataset.put("indication", indications.getSelected());

		super.getResponse().addData(dataset);
	}
}
