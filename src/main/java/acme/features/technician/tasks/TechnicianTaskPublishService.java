
package acme.features.technician.tasks;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskType;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianTaskPublishService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTaskRepository repository;


	@Override
	public void authorise() {
		Technician technician;
		Task task;
		int id;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTaskById(id);

		super.getResponse().setAuthorised(task.getTechnician().equals(technician));
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
	public void bind(final Task task) {
		// int technicianId = super.getRequest().getData("technician", int.class);
		//Technician technician = this.repository.findTechnicianById(technicianId);

		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
		//task.setTechnician(technician);
	}

	@Override
	public void validate(final Task task) {
		;
	}

	@Override
	public void perform(final Task task) {
		task.setDraftMode(false);
		this.repository.save(task);
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
