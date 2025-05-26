
package acme.features.flightCrewMembers.flightAssignments;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
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
		boolean invalidLeg;
		List<AbstractEntity> legs = this.repository.findAll();
		List<Integer> legsIds = legs.stream().map(l -> l.getId()).toList();
		Integer legId = super.getRequest().getData("leg", int.class);
		if (legId == 0)
			invalidLeg = true;
		else if (!legsIds.contains(legId))
			invalidLeg = false;
		else
			invalidLeg = true;
		int memberId;
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status = assignment.isDraftMode() && assignment.getFlightCrewMember().getId() == memberId;

		super.getResponse().setAuthorised(status && invalidLeg);

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
		List<ActivityLog> logs = this.repository.findLogsByAssignmentId(assignment.getId());
		this.repository.deleteAll(logs);
		this.repository.delete(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices choicesLegs;
		SelectChoices choicesDuties;
		SelectChoices choicesStatuses;
		Collection<Leg> legs = this.repository.findAllLegs();

		choicesLegs = SelectChoices.from(legs, "id", super.getRequest().getData("leg", int.class) == 0 ? null : assignment.getLeg());
		choicesDuties = SelectChoices.from(Duty.class, assignment.getDuty());
		choicesStatuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "leg", "currentStatus", "remarks", "draftMode");
		dataset.put("legs", choicesLegs);
		dataset.put("completed", super.getRequest().getData("leg", int.class) == 0 ? false : MomentHelper.isBefore(assignment.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment()));
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getEmployeeCode());
		dataset.put("duties", choicesDuties);
		dataset.put("statuses", choicesStatuses);
		super.getResponse().addData(dataset);
	}

}
