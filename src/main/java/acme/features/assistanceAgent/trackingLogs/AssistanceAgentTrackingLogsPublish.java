
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
public class AssistanceAgentTrackingLogsPublish extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		boolean status = trackingLog.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		TrackingLog trackingLog;
		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.getTrackingLogById(id);
		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		int claimId = super.getRequest().getData("claim", int.class);
		Claim claim = this.claimRepository.findClaim(claimId);
		trackingLog.setClaim(claim);

		super.bindObject(trackingLog, "step", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean claimIsPublished = !trackingLog.getClaim().isDraftMode();
		super.state(claimIsPublished, "claim", "assistance-agent.tracking-logs.claim-have-not-been-published");

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setDraftMode(false);
		this.repository.save(trackingLog);
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

		super.getResponse().addData(dataset);
	}
}
