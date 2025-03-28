
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
public class FlightCrewMemberFlightAssignmentDeleteService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		FlightAssignment assignment;
		int id;
		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(id);

		boolean status = assignment.isDraftMode() && assignment.getDuty().equals(Duty.LEAD_ATTENDANT);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;
		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findAssignmentById(id);

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
		assignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.delete(assignment);
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

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "leg", "currentStatus", "remarks", "draftMode");
		dataset.put("legs", choicesLegs);
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getEmployeeCode());
		dataset.put("duties", choicesDuties);
		dataset.put("statuses", choicesStatuses);
		super.getResponse().addData(dataset);
	}

}
