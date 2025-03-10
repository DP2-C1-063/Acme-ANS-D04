
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Boolean trackingLogCorrectlyFinished = trackingLog.getResolutionPercentage() == 100 && trackingLog.getStatus() != TrackingLogStatus.PENDING || trackingLog.getStatus() != TrackingLogStatus.PENDING && trackingLog.getResolution() != null;
			super.state(context, trackingLogCorrectlyFinished, "status", "acme.validation.aircraft.duplicated-RegistrationNumber.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
