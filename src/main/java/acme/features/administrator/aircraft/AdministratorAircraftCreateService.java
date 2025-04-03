
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airlines.Airlines;
import acme.features.administrator.airlines.AdministratorAirlinesRepository;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository	repository;

	@Autowired
	private AdministratorAirlinesRepository	airlineRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		Aircraft aircraft;
		aircraft = new Aircraft();

		super.getBuffer().addData(aircraft);
	}
	@Override
	public void bind(final Aircraft aircraft) {
		int airlineId;
		Airlines airline;
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.airlineRepository.findAirlineById(airlineId);
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices choicesAirline;
		SelectChoices choicesStatus;
		Dataset dataset;
		Collection<Airlines> airlines = this.airlineRepository.findAllAirlines();
		choicesAirline = SelectChoices.from(airlines, "id", aircraft.getAirline());
		choicesStatus = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");

		dataset.put("airlines", choicesAirline);

		dataset.put("statuses", choicesStatus);
		super.getResponse().addData(dataset);
	}

}
