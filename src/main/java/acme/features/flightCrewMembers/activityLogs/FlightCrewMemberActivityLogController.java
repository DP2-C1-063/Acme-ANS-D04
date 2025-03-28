
package acme.features.flightCrewMembers.activityLogs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activityLog.ActivityLog;
import acme.realms.flightCrewMembers.FlightCrewMembers;

@GuiController
public class FlightCrewMemberActivityLogController extends AbstractGuiController<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogListService		listService;

	@Autowired
	private FlightCrewMemberActivityLogShowService		showService;

	@Autowired
	private FlightCrewMemberActivityLogCreateService	createService;

	@Autowired
	private FlightCrewMemberActivityLogPublishService	publishService;

	@Autowired
	private FlightCrewMemberActivityLogUpdateService	updateService;

	@Autowired
	private FlightCrewMemberActivityLogDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
