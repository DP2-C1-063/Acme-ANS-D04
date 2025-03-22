
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;

@GuiService
public class AdministratorAirportCreateService extends AbstractGuiService<Administrator, Airport> {
	// Internal state

	@Autowired
	private AdministratorAirportRepository repository;


	// AbstractGuiService interface
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airport airport;
		airport = new Airport();

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "IATACode", "operationalScope", "city", "country", "website", "email", "address", "contactPhoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		;
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;

		dataset = super.unbindObject(airport, "name", "IATACode", "operationalScope", "city", "country", "website", "email", "address", "contactPhoneNumber");

		super.getResponse().addData(dataset);
	}

}
