
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository	repository;

	@Autowired
	private TrackingLogRepository			trackingLogRepository;


	@Override
	public void authorise() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaim(id);
		boolean status = claim.isDraftMode();
		AssistanceAgent agent;
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		status = status && claim.getAssistanceAgent().equals(agent);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaim(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "passengerEmail", "description", "type");
		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);
		claim.setLeg(leg);

	}

	@Override
	public void validate(final Claim claim) {
		boolean trackingLogPublished = true;
		Collection<TrackingLog> trackinglogs = this.trackingLogRepository.getLastTrackingLogByClaim(claim.getId());
		for (TrackingLog tl : trackinglogs)
			trackingLogPublished = trackingLogPublished && tl.isDraftMode();
		super.state(!trackingLogPublished, "*", "assistance-agent.claim.tracking-log-have-not-been-published");

	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> trackinglogs = this.trackingLogRepository.getLastTrackingLogByClaim(claim.getId());
		for (TrackingLog tl : trackinglogs)
			this.trackingLogRepository.delete(tl);
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices choicesLegs;
		SelectChoices choicesTypes;
		choicesTypes = SelectChoices.from(ClaimType.class, claim.getType());
		Collection<Leg> legs = this.repository.findAllLegs();
		choicesLegs = SelectChoices.from(legs, "scheduledArrival", claim.getLeg());
		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg");
		dataset.put("legs", choicesLegs);
		dataset.put("assistanceAgent", claim.getAssistanceAgent().getEmployeeCode());
		dataset.put("types", choicesTypes);
		super.getResponse().addData(dataset);
	}

}
