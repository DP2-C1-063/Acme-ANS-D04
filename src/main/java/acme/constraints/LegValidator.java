
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

public class LegValidator extends AbstractValidator<ValidLeg, Leg> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private LegRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueLeg;
				Leg existingLeg;

				existingLeg = this.repository.findLegByFlightNumber(leg.getFlightNumber());
				uniqueLeg = existingLeg == null || existingLeg.equals(leg);

				super.state(context, uniqueLeg, "flightNumber", "acme.validation.leg.duplicated-flight-number.message");
			}
			{
				boolean arrivalIsBeforeDeparture;
				arrivalIsBeforeDeparture = MomentHelper.isBefore(leg.getScheduledArrival(), leg.getScheduledDeparture());

				super.state(context, !arrivalIsBeforeDeparture, "scheduledArrival", "acme.validation.leg.arrival-before-departure.message");
			}
			{
				// Added based on a teacher's message in the discussion forum. Max duration = 1000 minutes (aprox. 16.667 hours)
				boolean durationIsTooLong;
				durationIsTooLong = leg.getDuration() > 16.667;

				super.state(context, !durationIsTooLong, "duration", "acme.validation.leg.duration-too-long.message");
			}
			{
				// Added based on a teacher's message in the discussion forum. Min duration = 1 minute (aprox. 0.016 hours)
				boolean durationIsTooShort;
				durationIsTooShort = leg.getDuration() < 0.016;

				super.state(context, !durationIsTooShort, "duration", "acme.validation.leg.duration-too-short.message");
			}
			{
				// Puede que deba eliminarse esta restricción
				boolean sameDepartureAndArrivalAirport;
				sameDepartureAndArrivalAirport = leg.getDepartureAirport().equals(leg.getArrivalAirport());

				super.state(context, !sameDepartureAndArrivalAirport, "arrivalAirport", "acme.validation.leg.same-departure-and-arrival-airport.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
