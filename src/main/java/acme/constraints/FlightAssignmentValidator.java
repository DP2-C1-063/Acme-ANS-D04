
package acme.constraints;

import java.util.Collection;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentRepository;
import acme.entities.leg.Leg;
import acme.realms.flightCrewMembers.AvailabilityStatus;

public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment assignment, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (assignment == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else if (assignment.getFlightCrewMember() != null && assignment.getLeg() != null) {
			{
				boolean availableMember;
				availableMember = assignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
				super.state(context, availableMember, "flightCrewMember", "acme.validation.flightAssignment.member-not-available.message");

			}
			{
				boolean noConcurrentLegs;
				Collection<Leg> cocurrentLegs = this.repository.findAllConcurrentLegs(assignment.getFlightCrewMember().getId(), assignment.getLeg().getScheduledDeparture(), assignment.getLeg().getScheduledArrival(), assignment.getLeg().getId());
				noConcurrentLegs = cocurrentLegs.isEmpty();
				super.state(context, noConcurrentLegs, "leg", "acme.validation.flightAssignment.concurrent-leg.message");

			}
			{
				boolean onlyPilot;
				Collection<FlightAssignment> existingPilot = this.repository.findDutyAlreadyInLeg(Duty.PILOT, assignment.getLeg().getId(), assignment.getFlightCrewMember().getId());
				onlyPilot = !assignment.getDuty().equals(Duty.PILOT) || existingPilot.isEmpty();
				super.state(context, onlyPilot, "duty", "acme.validation.flightAssignment.already-pilot");
			}
			{
				boolean onlyCoPilot;
				Collection<FlightAssignment> existingCoPilot = this.repository.findDutyAlreadyInLeg(Duty.CO_PILOT, assignment.getLeg().getId(), assignment.getFlightCrewMember().getId());
				onlyCoPilot = !assignment.getDuty().equals(Duty.CO_PILOT) || existingCoPilot.isEmpty();
				super.state(context, onlyCoPilot, "duty", "acme.validation.flightAssignment.already-co-pilot");

			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
