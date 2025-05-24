
package acme.features.assistanceAgent.trackingLogs;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.features.assistanceAgent.claim.AssistanceAgentClaimRepository;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogsCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogsRepository	repository;

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;


	@Override
	public void authorise() {
		int masterId;

		Claim claim;

		String method = super.getRequest().getMethod();
		boolean status = true;
		if (method.equals("POST")) {
			int id = super.getRequest().getData("id", int.class);
			status = id == 0;

		}

		masterId = super.getRequest().getData("masterId", int.class);

		claim = this.repository.findClaim(masterId);
		AssistanceAgent agent;
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		status = status && claim != null && claim.getAssistanceAgent().equals(agent);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog = new TrackingLog();
		AssistanceAgent agent;
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		trackingLog.setAssistanceAgent(agent);
		trackingLog.setStatus(TrackingLogStatus.PENDING);
		int claimId;
		Claim claim;

		claim = this.repository.findClaim(super.getRequest().getData("masterId", int.class));
		trackingLog.setClaim(claim);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {

		super.bindObject(trackingLog, "step", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		;
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		if (trackingLog.getClaim().isReview()) {
			Claim claim = trackingLog.getClaim();
			claim.setDraftMode(false);
			claim.setReview(false);
			this.claimRepository.save(claim);
		}
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices choicesStatus;
		Dataset dataset;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "status", "resolution");
		dataset.put("claim", trackingLog.getClaim());
		dataset.put("statuses", choicesStatus);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		super.getResponse().addData(dataset);
	}
}
