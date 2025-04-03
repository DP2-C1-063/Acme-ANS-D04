
package acme.features.technician.taskInvolvesRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.tasks.TaskInvolvesRecord;
import acme.realms.technician.Technician;

@GuiController
public class TechnicianTaskInvolvesRecordController extends AbstractGuiController<Technician, TaskInvolvesRecord> {

	@Autowired
	private TechnicianTaskInvolvesRecordListService		listService;

	@Autowired
	private TechnicianTaskInvolvesRecordShowService		showService;

	@Autowired
	private TechnicianTaskInvolvesRecordCreateService	createService;

	@Autowired
	private TechnicianTaskInvolvesRecordDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
