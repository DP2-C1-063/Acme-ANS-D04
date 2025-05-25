
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordStatus;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecord();
		maintenanceRecord.setStatus(MaintenanceRecordStatus.PENDING);
		maintenanceRecord.setMaintenanceMoment(MomentHelper.getCurrentMoment());

		var money = new Money();
		money.setAmount(0.);
		money.setCurrency("EUR");
		maintenanceRecord.setEstimatedCost(money);

		maintenanceRecord.setNotes("");
		maintenanceRecord.setRelatedAircraft(null);
		maintenanceRecord.setTechnician(technician);
		maintenanceRecord.setDraftMode(true);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		int registrationNumber = super.getRequest().getData("relatedAircraft", int.class);
		Aircraft aircraft = this.repository.findAircraftById(registrationNumber);

		super.bindObject(maintenanceRecord, "status", "nextInspection", "estimatedCost", "notes");
		maintenanceRecord.setRelatedAircraft(aircraft);

	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		SelectChoices choicesStatuses;
		SelectChoices choicesAircraft;

		choicesStatuses = SelectChoices.from(MaintenanceRecordStatus.class, maintenanceRecord.getStatus());
		Collection<Aircraft> aircrafts = this.repository.findAllAircraft();
		choicesAircraft = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getRelatedAircraft());

		dataset = super.unbindObject(maintenanceRecord, "status", "nextInspection", "estimatedCost", "notes", "draftMode");
		dataset.put("relatedAircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("statuses", choicesStatuses);

		super.getResponse().addData(dataset);
	}
}
