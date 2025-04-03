
package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.features.assistanceAgent.claim.AssistanceAgentClaimRepository;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackigLogsShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogsRepository	repository;

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;


	@Override
	public void authorise() {
		int id;
		TrackingLog trackingLog;
		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.getTrackingLogById(id);
		AssistanceAgent agent;
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		Boolean status = trackingLog.getAssistanceAgent().equals(agent);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.getTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices choicesStatus;
		Dataset dataset;
		Collection<Claim> claims = this.claimRepository.findAllClaimsByAssistanceAgent(trackingLog.getAssistanceAgent().getId());
		SelectChoices choicesClaims = SelectChoices.from(claims, "id", trackingLog.getClaim());

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "status", "resolution", "draftMode");
		dataset.put("claims", choicesClaims);
		dataset.put("statuses", choicesStatus);
		dataset.put("lastUpdateMoment", trackingLog.getLastUpdateMoment());
		dataset.put("assistanceAgent", trackingLog.getAssistanceAgent().getEmployeeCode());
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		super.getResponse().addData(dataset);
	}
}
