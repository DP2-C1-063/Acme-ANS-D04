
package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.features.assistanceAgent.claim.AssistanceAgentClaimRepository;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogsUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogsRepository	repository;

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;


	@Override
	public void authorise() {
		int id;
		id = super.getRequest().getData("id", int.class);
		TrackingLog trackingLog;
		trackingLog = this.repository.getTrackingLogById(id);

		AssistanceAgent agent;
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = trackingLog != null && trackingLog.getAssistanceAgent().equals(agent) && trackingLog.isDraftMode();
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

		super.bindObject(trackingLog, "step", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		if (trackingLog.getResolutionPercentage() != null) {
			Boolean percentageIsAscendance = true;
			Collection<TrackingLog> trackingLogs = this.repository.getAllTrackingLogsByClaim(trackingLog.getClaim().getId());
			if (!trackingLogs.isEmpty())
				percentageIsAscendance = trackingLogs.stream().toList().get(0).getResolutionPercentage() < trackingLog.getResolutionPercentage() || this.repository.getAllTrackingLogsByClaim(trackingLog.getClaim().getId()).isEmpty()
					|| trackingLogs.contains(trackingLog);
			super.state(percentageIsAscendance, "resolutionPercentage", "acme.validation.trackinglog.resolution-percentage.message");
		}

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices choicesStatus;
		Dataset dataset;

		choicesStatus = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "status", "resolution", "draftMode");

		dataset.put("statuses", choicesStatus);

		super.getResponse().addData(dataset);
	}
}
