
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.maintenanceRecords.MaintenanceRecordStatus;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;

		if (super.getRequest().getMethod().equals("POST")) {
			MaintenanceRecord mrecord;
			Technician technician;
			int id;

			if (super.getRequest().hasData("id")) {
				id = super.getRequest().getData("id", int.class);
				mrecord = this.repository.findMaintenanceRecordById(id);

				if (mrecord != null) {
					status = mrecord.isDraftMode();

					Collection<Integer> aircrafts = this.repository.findAllAircraft().stream().map(Aircraft::getId).toList();

					technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
					status = status && mrecord.getTechnician().equals(technician);

					int relatedAircraft = super.getRequest().getData("relatedAircraft", int.class);
					status = status && (relatedAircraft == 0 || aircrafts.contains(relatedAircraft));
				} else
					status = false;
			} else
				status = false;
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		int registrionNumber = super.getRequest().getData("relatedAircraft", int.class);
		Aircraft aircraft = this.repository.findAircraftById(registrionNumber);

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

		dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "status", "nextInspection", "estimatedCost", "notes", "draftMode");
		dataset.put("relatedAircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("statuses", choicesStatuses);

		super.getResponse().addData(dataset);
	}

}
