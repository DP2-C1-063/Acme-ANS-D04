
package acme.features.technician.taskInvolvesRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskInvolvesRecord;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianTaskInvolvesRecordCreateService extends AbstractGuiService<Technician, TaskInvolvesRecord> {

	@Autowired
	private TechnicianTaskInvolvesRecordRepository repository;


	@Override
	public void authorise() {
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		int masterId = super.getRequest().getData("masterId", int.class);
		MaintenanceRecord record = this.repository.findMaintenanceRecordById(masterId);

		boolean isAuthorised = record.getTechnician().equals(technician);

		if (super.getRequest().getData().containsKey("id")) {
			int taskId = super.getRequest().getData("task", int.class);
			Task task = this.repository.findTaskById(taskId);

			boolean isTechnician = task.getTechnician().equals(technician);
			boolean isNotDraft = !task.isDraftMode();

			isAuthorised = isAuthorised && (isTechnician || isNotDraft);
		}

		if (!record.isDraftMode())
			isAuthorised = false;

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		TaskInvolvesRecord involves;
		MaintenanceRecord record;

		record = this.repository.findMaintenanceRecordById(super.getRequest().getData("masterId", int.class));

		involves = new TaskInvolvesRecord();
		involves.setTask(null);
		involves.setMaintenanceRecord(record);

		super.getBuffer().addData(involves);
	}

	@Override
	public void bind(final TaskInvolvesRecord involves) {
		int taskId = super.getRequest().getData("task", int.class);
		Task task = this.repository.findTaskById(taskId);
		involves.setTask(task);
	}

	@Override
	public void validate(final TaskInvolvesRecord involves) {
		;
	}

	@Override
	public void perform(final TaskInvolvesRecord involves) {
		this.repository.save(involves);
	}

	@Override
	public void unbind(final TaskInvolvesRecord involves) {
		Dataset dataset;
		SelectChoices choicesTask;
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		Collection<Task> tasks = this.repository.findAllTasks().stream().filter(f -> !f.isDraftMode() || f.getTechnician().equals(technician)).toList();

		choicesTask = SelectChoices.from(tasks, "description", involves.getTask());

		dataset = super.unbindObject(involves);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("task", choicesTask.getSelected().getKey());
		dataset.put("tasks", choicesTask);

		super.getResponse().addData(dataset);
	}
}
