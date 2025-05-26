
package acme.features.technician.taskInvolvesRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskInvolvesRecord;
import acme.entities.tasks.TaskType;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianTaskInvolvesRecordShowService extends AbstractGuiService<Technician, TaskInvolvesRecord> {

	@Autowired
	private TechnicianTaskInvolvesRecordRepository repository;


	@Override
	public void authorise() {

		TaskInvolvesRecord involves;
		Technician technician;
		int id;
		boolean status = true;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		if (super.getRequest().hasData("id")) {
			id = super.getRequest().getData("id", int.class);
			involves = this.repository.findTaskInvolvesRecordById(id);

			boolean isTechnician = involves.getTask().getTechnician().equals(technician);
			boolean isNotDraft = !involves.getTask().isDraftMode();

			status = isTechnician || isNotDraft;

		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TaskInvolvesRecord involves;

		int id;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findTaskInvolvesRecordById(id);

		super.getBuffer().addData(involves);
	}

	@Override
	public void unbind(final TaskInvolvesRecord involves) {
		Dataset dataset;
		SelectChoices choicesType;
		SelectChoices choicesTask;

		choicesType = SelectChoices.from(TaskType.class, involves.getTask().getType());
		Collection<Task> tasks = this.repository.findAllTasks();
		choicesTask = SelectChoices.from(tasks, "id", involves.getTask());

		dataset = super.unbindObject(involves, "maintenanceRecord.relatedAircraft.registrationNumber", "task.type", "task.priority", "task.estimatedDuration", "task.description", "task.technician.licenseNumber");
		dataset.put("task", choicesTask.getSelected().getKey());
		dataset.put("tasks", choicesTask);
		dataset.put("types", choicesType);

		dataset.put("draftMode", involves.getMaintenanceRecord().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
