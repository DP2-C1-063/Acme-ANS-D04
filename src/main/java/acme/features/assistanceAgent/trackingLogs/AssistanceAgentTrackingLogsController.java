
package acme.features.assistanceAgent.trackingLogs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiController
public class AssistanceAgentTrackingLogsController extends AbstractGuiController<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogsListService		listService;
	@Autowired
	private AssistanceAgentTrackigLogsShowService		showService;
	@Autowired
	private AssistanceAgentTrackingLogsCreateService	createService;
	@Autowired
	private AssistanceAgentTrackingLogsUpdateService	updateService;
	@Autowired
	private AssistanceAgentTrackingLogsDeleteService	deleteService;

	@Autowired
	private AssistanceAgentTrackingLogsPublish			publishService;


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
