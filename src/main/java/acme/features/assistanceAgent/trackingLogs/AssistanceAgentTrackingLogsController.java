
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


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);

	}
}
