
package acme.features.assistanceAgent.claim;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPendingListService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		Collection<Claim> res = new ArrayList<>();
		int memberId;
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		claims = this.repository.findAllClaimsByAssistanceAgent(memberId);
		for (Claim c : claims)
			if (c.getStatus().equals("PENDING"))
				res.add(c);

		super.getBuffer().addData(res);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "passengerEmail", "type", "description");

		super.getResponse().addData(dataset);
	}

}
