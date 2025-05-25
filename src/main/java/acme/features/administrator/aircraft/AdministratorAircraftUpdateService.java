
package acme.features.administrator.aircraft;

import java.util.Collection;
import java.util.List;

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
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository	repository;

	@Autowired
	private AdministratorAirlinesRepository	airlineRepository;


	@Override
	public void authorise() {
		boolean status;
		boolean correctAirline = true;

		if (super.getRequest().getMethod().equals("POST")) {
			int id;
			Aircraft aircraft;

			id = super.getRequest().getData("id", int.class);
			aircraft = this.repository.findAircraftById(id);
			status = aircraft != null;
			Integer airlineId = super.getRequest().getData("airline", int.class);
			List<Integer> airlinesIds = this.airlineRepository.findAllAirlines().stream().map(a -> a.getId()).toList();
			if (airlineId == 0)
				correctAirline = true;
			else if (!airlinesIds.contains(airlineId))
				correctAirline = false;
		} else
			status = false;

		super.getResponse().setAuthorised(status && correctAirline);
	}
	@Override
	public void load() {
		Aircraft aircraft;

		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

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
