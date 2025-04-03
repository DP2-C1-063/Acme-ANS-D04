
package acme.features.technician.tasks;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.tasks.Task;
import acme.realms.technician.Technician;

@GuiController
public class TechnicianTaskController extends AbstractGuiController<Technician, Task> {

	@Autowired
	private TechnicianTaskListService		listService;

	@Autowired
	private TechnicianTaskShowService		showService;

	@Autowired
	private TechnicianTaskUpdateService	updateService;

	@Autowired
	private TechnicianTaskPublishService	publishService;

	@Autowired
	private TechnicianTaskCreateService	createService;

	@Autowired
	private TechnicianTaskDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}
}
