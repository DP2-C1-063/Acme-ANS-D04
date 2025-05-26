
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
public class TechnicianTaskInvolvesRecordDeleteService extends AbstractGuiService<Technician, TaskInvolvesRecord> {

	@Autowired
	private TechnicianTaskInvolvesRecordRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		if (super.getRequest().getMethod().equals("POST")) {
			if (super.getRequest().hasData("id")) {
				int id = super.getRequest().getData("id", int.class);
				TaskInvolvesRecord mrecord = this.repository.findTaskInvolvesRecordById(id);
				if (mrecord != null)
					status = mrecord.getMaintenanceRecord().isDraftMode() && mrecord.getMaintenanceRecord().getTechnician().equals(technician);
				else
					status = false;
			} else
				status = false;
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void bind(final TaskInvolvesRecord involves) {
		;
	}

	@Override
	public void validate(final TaskInvolvesRecord involves) {
		;
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
	public void perform(final TaskInvolvesRecord involves) {
		this.repository.delete(involves);

	}

	@Override
	public void unbind(final TaskInvolvesRecord involves) {
		Dataset dataset;
		SelectChoices choicesType;
		SelectChoices choicesTask;

		choicesType = SelectChoices.from(TaskType.class, involves.getTask().getType());
		Collection<Task> tasks = this.repository.findAllTasks();
		choicesTask = SelectChoices.from(tasks, "id", involves.getTask());

		dataset = super.unbindObject(involves, "task.type", "task.priority", "task.estimatedDuration", "task.description");
		dataset.put("task", choicesTask.getSelected().getKey());
		dataset.put("tasks", choicesTask);
		dataset.put("types", choicesType);
		super.addPayload(dataset, involves, "task.technician.licenseNumber");
		super.getResponse().addData(dataset);

	}
}
