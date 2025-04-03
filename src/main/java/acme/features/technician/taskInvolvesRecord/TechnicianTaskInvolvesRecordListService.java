
package acme.features.technician.taskInvolvesRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tasks.TaskInvolvesRecord;
import acme.entities.tasks.TaskType;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianTaskInvolvesRecordListService extends AbstractGuiService<Technician, TaskInvolvesRecord> {

	@Autowired
	private TechnicianTaskInvolvesRecordRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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

		dataset = super.unbindObject(involves, "task.type", "task.priority", "task.estimatedDuration");
		dataset.put("types", choicesType);
		super.addPayload(dataset, involves, "task.technician.licenseNumber");
		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TaskInvolvesRecord> involvesColl) {
		Dataset dataset;
		SelectChoices choicesType;

		super.getResponse().addGlobal("masterId", super.getRequest().getData("masterId", int.class));
	}

}
