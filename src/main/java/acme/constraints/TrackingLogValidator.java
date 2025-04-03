
package acme.constraints;

import java.util.List;

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
			boolean statusCorrect = trackingLog.getResolutionPercentage() == 100.00 && trackingLog.getStatus() != TrackingLogStatus.PENDING || trackingLog.getResolutionPercentage() != 100.00 && trackingLog.getStatus() == TrackingLogStatus.PENDING;
			boolean resolutionCorrect = trackingLog.getStatus() != TrackingLogStatus.PENDING && trackingLog.getResolution() != null;
			Boolean trackingLogCorrectlyFinished = statusCorrect || resolutionCorrect;
			super.state(context, trackingLogCorrectlyFinished, "status", "acme.validation.trackinglog.status.message");
			if (!trackingLog.getClaim().isReview()) {
				Boolean percentageIsAscendance;
				List<TrackingLog> trackingLogs = this.trackingLogRepository.getLastTrackingLogByClaim(trackingLog.getClaim().getId());
				percentageIsAscendance = trackingLogs.get(0).getResolutionPercentage() < trackingLog.getResolutionPercentage() || this.trackingLogRepository.getLastTrackingLogByClaim(trackingLog.getClaim().getId()).isEmpty()
					|| trackingLogs.contains(trackingLog);
				super.state(context, percentageIsAscendance, "resolutionPercentage", "acme.validation.trackinglog.resolution-percentage.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
