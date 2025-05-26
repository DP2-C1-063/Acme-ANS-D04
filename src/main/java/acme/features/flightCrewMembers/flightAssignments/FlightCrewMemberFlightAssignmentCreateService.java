
package acme.features.flightCrewMembers.flightAssignments;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
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
		boolean auth;

		boolean transientId = true;
		boolean correctDuty = true;
		boolean correctStatus = true;
		if (super.getRequest().getMethod().equals("POST")) {
			String enumValue = super.getRequest().getData("duty", String.class);
			correctDuty = Arrays.stream(Duty.values()).anyMatch(d -> d.toString().equals(enumValue));
			correctDuty = correctDuty || enumValue.equals("0");
		}
		if (super.getRequest().getMethod().equals("POST")) {
			String enumValue = super.getRequest().getData("currentStatus", String.class);
			correctStatus = Arrays.stream(CurrentStatus.values()).anyMatch(s -> s.toString().equals(enumValue));
			correctStatus = correctStatus || enumValue.equals("0");
		}
		if (super.getRequest().getMethod().equals("POST") && super.getRequest().getData("id", int.class) != 0)
			transientId = false;
		if (super.getRequest().getMethod().equals("GET") && super.getRequest().getData().containsKey("id"))
			transientId = false;

		if (super.getRequest().getMethod().equals("POST")) {

			if (super.getRequest().getData().containsKey("leg")) {

				List<AbstractEntity> legs = this.repository.findAll();
				List<Integer> legsIds = legs.stream().map(l -> l.getId()).toList();
				Integer legId = super.getRequest().getData("leg", int.class);
				if (legId == 0)
					auth = true;
				else if (!legsIds.contains(legId))
					auth = false;
				else
					auth = true;
			} else
				auth = false;
		} else
			auth = true;
		super.getResponse().setAuthorised(auth && transientId && correctDuty && correctStatus);
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
		choicesDuties = SelectChoices.from(Duty.class, assignment.getDuty() == null ? null : assignment.getDuty());
		choicesStatuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus() == null ? null : assignment.getCurrentStatus());

		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "leg", "currentStatus", "remarks");
		dataset.put("legs", choicesLegs);
		dataset.put("flightCrewMember", assignment.getFlightCrewMember().getEmployeeCode());
		dataset.put("duties", choicesDuties);
		dataset.put("statuses", choicesStatuses);
		super.getResponse().addData(dataset);
	}

}
