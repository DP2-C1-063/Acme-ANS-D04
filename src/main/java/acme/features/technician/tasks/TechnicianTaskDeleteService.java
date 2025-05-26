
package acme.features.technician.tasks;

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
public class TechnicianTaskDeleteService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		if (super.getRequest().getMethod().equals("POST")) {
			Task task;
			int id;
			Technician technician;
			technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

			id = super.getRequest().getData("id", int.class);
			task = this.repository.findTaskById(id);

			status = task != null && task.isDraftMode() && task.getTechnician().equals(technician);
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void bind(final Task task) {

		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {
		;
	}

	@Override
	public void load() {
		Task task;
		int id;

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void perform(final Task task) {
		Collection<TaskInvolvesRecord> involves = this.repository.findAllInvolvesAssociatedWith(task.getId());

		for (TaskInvolvesRecord i : involves)
			this.repository.delete(i);

		this.repository.delete(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices choicesType;

		choicesType = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("types", choicesType);
		super.getResponse().addData(dataset);
	}
}
