
package acme.features.administrator.airlines;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.AirlineType;
import acme.entities.airlines.Airlines;

@GuiService
public class AdministratorAirlinesUpdateService extends AbstractGuiService<Administrator, Airlines> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlinesRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean validEnum = false;
		if (super.getRequest().getMethod().equals("POST")) {
			int id;
			Airlines airlines;

			id = super.getRequest().getData("id", int.class);
			airlines = this.repository.findAirlineById(id);
			status = airlines != null;
			String enumValue = super.getRequest().getData("type", String.class);
			validEnum = Arrays.stream(AirlineType.values()).anyMatch(t -> t.toString().equals(enumValue));
			validEnum = validEnum || enumValue.equals("0");
		} else
			status = false;

		super.getResponse().setAuthorised(status && validEnum);
	}

	@Override
	public void load() {
		Airlines airline;
		int id;

		id = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(id);

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
