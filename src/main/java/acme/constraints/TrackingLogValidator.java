
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.entities.trackingLogs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private TrackingLogRepository trackingLogRepository;


	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			Boolean trackingLogCorrectlyFinished = trackingLog.getResolutionPercentage() == 100 && trackingLog.getStatus() != TrackingLogStatus.PENDING || trackingLog.getStatus() != TrackingLogStatus.PENDING && trackingLog.getResolution() != null;
			super.state(context, trackingLogCorrectlyFinished, "status", "acme.validation.trackinglog.status.message");
			Boolean percentageIsAscendance;
			percentageIsAscendance = this.trackingLogRepository.getLastTrackingRepositoryByClaim(trackingLog.getClaim().getId()).get(0).getResolutionPercentage() <= trackingLog.getResolutionPercentage();
			super.state(context, percentageIsAscendance, "status", "acme.validation.trackinglog.resolution-percentage.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
