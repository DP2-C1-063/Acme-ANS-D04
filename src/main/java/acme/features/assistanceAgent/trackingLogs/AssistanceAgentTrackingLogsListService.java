
package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogsListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogsRepository repository;


	@Override
	public void authorise() {
		int masterId;

		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaim(masterId);
		AssistanceAgent agent;
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		Boolean status = claim != null && (claim.getAssistanceAgent().equals(agent) || !claim.isDraftMode());
		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		Collection<TrackingLog> trackingLog;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);

		trackingLog = this.repository.getAllTrackingLogsByClaim(masterId);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "resolutionPercentage", "step", "lastUpdateMoment");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		super.getResponse().addGlobal("masterId", super.getRequest().getData("masterId", int.class));
		super.getResponse().addData(dataset);
	}
	@Override
	public void unbind(final Collection<TrackingLog> trackingLog) {

		final boolean showCreate;
		int masterId;
		Claim claim;
		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaim(masterId);
		showCreate = claim != null;
		super.getResponse().addGlobal("masterId", super.getRequest().getData("masterId", int.class));
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
