
package acme.features.technician.taskInvolvesRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.tasks.TaskInvolvesRecord;
import acme.entities.tasks.TaskType;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianTaskInvolvesRecordListService extends AbstractGuiService<Technician, TaskInvolvesRecord> {

	@Autowired
	private TechnicianTaskInvolvesRecordRepository repository;


	@Override
	public void authorise() {
		MaintenanceRecord mrecord;
		Technician technician;
		int id;

		boolean status = false;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		if (super.getRequest().hasData("masterId")) {

			id = super.getRequest().getData("masterId", int.class);
			mrecord = this.repository.findMaintenanceRecordById(id);

			if (mrecord != null)
				status = mrecord.getTechnician().equals(technician);
			else
				status = false;
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TaskInvolvesRecord> tasksInvolvesRecord;

		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		tasksInvolvesRecord = this.repository.findAllTaskAssociatedWith(masterId);

		super.getBuffer().addData(tasksInvolvesRecord);
	}

	@Override
	public void unbind(final TaskInvolvesRecord involves) {
		Dataset dataset;
		SelectChoices choicesType;

		super.getResponse().addGlobal("masterId", super.getRequest().getData("masterId", int.class));

		choicesType = SelectChoices.from(TaskType.class, involves.getTask().getType());

		dataset = super.unbindObject(involves, "task.type", "task.priority", "task.estimatedDuration", "task.description");
		dataset.put("types", choicesType);
		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TaskInvolvesRecord> involvesColl) {

		int masterId = super.getRequest().getData("masterId", int.class);
		MaintenanceRecord maintenanceRecord = this.repository.findMaintenanceRecordById(masterId);

		super.getResponse().addGlobal("draftMode", maintenanceRecord.isDraftMode());

		super.getResponse().addGlobal("masterId", masterId);
	}

}
