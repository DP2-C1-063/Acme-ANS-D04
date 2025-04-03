
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
public class ManagerFlightCreateService extends AbstractGuiService<Manager, Flight> {
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
		Flight flight;
		Manager manager;

		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		flight = new Flight();
		flight.setPublished(false);
		flight.setManager(manager);

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
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		SelectChoices indications;
		indications = SelectChoices.from(Indication.class, flight.getIndication());
		Manager manager;

		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "description", "published");
		dataset.put("indications", indications);
		dataset.put("indication", indications.getSelected());
		dataset.put("manager", manager);

		super.getResponse().addData(dataset);
	}
}
