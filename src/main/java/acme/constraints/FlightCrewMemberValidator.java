
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.realms.flightCrewMembers.FlightCrewMemberRepository;
import acme.realms.flightCrewMembers.FlightCrewMembers;

public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMembers> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMembers member, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (member == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean uniqueMember;
			FlightCrewMembers existingMember;

			existingMember = this.repository.findMemberByEmployeeCode(member.getEmployeeCode());
			uniqueMember = existingMember == null || existingMember.equals(member);

			super.state(context, uniqueMember, "employeeCode", "acme.validation.flightcrewmember.duplicated-employeecode.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
