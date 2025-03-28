
package acme.features.flightCrewMembers.flightAssignments;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.CurrentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.FlightCrewMembers;

@GuiService
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		FlightCrewMembers member;
		member = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();

		assignment = new FlightAssignment();
		assignment.setCurrentStatus(CurrentStatus.PENDING);
		assignment.setDraftMode(true);
		assignment.setDuty(Duty.PILOT);
		assignment.setFlightCrewMember(member);
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
		assignment.setLeg(null);
		assignment.setRemarks("");
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		int legId;
		Leg leg;
		super.bindObject(assignment, "duty", "currentStatus", "remarks");
		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		assignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices choicesLegs;
		SelectChoices choicesDuties;
		SelectChoices choicesStatuses;
		Collection<Leg> legs = this.repository.findAllLegs();
		choicesLegs = SelectChoices.from(legs, "id", assignment.getLeg());
		choicesDuties = SelectChoices.from(Duty.class, assignment.getDuty());
		choicesStatuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "leg", "currentStatus", "remarks");
		dataset.put("legs", choicesLegs);
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getEmployeeCode());
		dataset.put("duties", choicesDuties);
		dataset.put("statuses", choicesStatuses);
		super.getResponse().addData(dataset);
	}

}
