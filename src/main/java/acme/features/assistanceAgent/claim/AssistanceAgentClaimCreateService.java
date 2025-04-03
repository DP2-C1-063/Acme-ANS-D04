
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim claim = new Claim();
		AssistanceAgent agent;
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());
		claim.setAssistanceAgent(agent);

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
		boolean notYetOcurred;
		notYetOcurred = MomentHelper.isAfter(claim.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
		super.state(notYetOcurred, "leg", "flight-crew-member.flight-assignment.leg-has-not-finished-yet");

	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices choicesLegs;
		SelectChoices choicesTypes;
		choicesTypes = SelectChoices.from(ClaimType.class, claim.getType());
		Collection<Leg> legs = this.repository.findAllLegs();
		choicesLegs = SelectChoices.from(legs, "id", claim.getLeg());
		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg");
		dataset.put("legs", choicesLegs);
		dataset.put("assistanceAgent", claim.getAssistanceAgent().getEmployeeCode());
		dataset.put("types", choicesTypes);
		super.getResponse().addData(dataset);
	}
}
