
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (claim == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		else if (claim.getLeg() != null) {

			boolean landedLeg;
			Date currentMoment = MomentHelper.getCurrentMoment();
			Leg leg = claim.getLeg();
			landedLeg = MomentHelper.isBefore(leg.getScheduledArrival(), currentMoment);

			super.state(context, landedLeg, "leg", "assistance-agent.claim.leg-has-not-finished-yet");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
