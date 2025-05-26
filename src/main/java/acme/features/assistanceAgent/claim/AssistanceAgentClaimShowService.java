
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
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		int id;
		Claim claim;
		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaim(id);
		AssistanceAgent agent;
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = claim != null && claim.getAssistanceAgent().equals(agent);
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
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices choicesLegs;
		SelectChoices choicesTypes;

		choicesTypes = SelectChoices.from(ClaimType.class, claim.getType());
		Collection<Leg> legs = this.repository.findAllLegs();
		choicesLegs = SelectChoices.from(legs, "scheduledArrival", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg", "draftMode");
		dataset.put("legs", choicesLegs);
		dataset.put("assistanceAgent", claim.getAssistanceAgent().getEmployeeCode());
		dataset.put("types", choicesTypes);
		dataset.put("status", claim.getStatus());
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		super.getResponse().addData(dataset);
	}
}
