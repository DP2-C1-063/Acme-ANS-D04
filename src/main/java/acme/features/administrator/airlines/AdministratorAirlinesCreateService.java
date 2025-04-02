
package acme.features.administrator.airlines;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.AirlineType;
import acme.entities.airlines.Airlines;

@GuiService
public class AdministratorAirlinesCreateService extends AbstractGuiService<Administrator, Airlines> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlinesRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airlines airline;

		airline = new Airlines();
		airline.setIATACode("");
		airline.setEmail("");
		airline.setFoundationMoment(null);
		airline.setName("");
		airline.setPhoneNumber("");
		airline.setWeb("");
		airline.setType(AirlineType.STANDARD);

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airlines airline) {

		super.bindObject(airline, "name", "IATACode", "web", "type", "email", "phoneNumber", "foundationMoment");

	}

	@Override
	public void validate(final Airlines airline) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airlines airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airlines airline) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(AirlineType.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "IATACode", "web", "type", "email", "phoneNumber", "foundationMoment");
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
