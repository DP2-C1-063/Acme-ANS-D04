
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.activityLog.ActivityLog;
import acme.entities.activityLog.ActivityLogRepository;
import acme.entities.leg.Leg;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Autowired
	private ActivityLogRepository activityLogRepository;


	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (activityLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		else if (activityLog.getFlightAssignment() != null) {

			boolean landedLeg;
			Date currentMoment = MomentHelper.getCurrentMoment();
			Leg legOfLog = activityLog.getFlightAssignment().getLeg();
			landedLeg = MomentHelper.isBefore(legOfLog.getScheduledArrival(), currentMoment);

			super.state(context, landedLeg, "flightAssignment", "acme.validation.activityLog.leg-not-done-yet.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
