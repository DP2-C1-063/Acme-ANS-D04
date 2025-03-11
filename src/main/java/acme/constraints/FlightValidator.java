
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;

public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (flight == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean arrivalIsBeforeDeparture;
			Flight existingFlight;

			arrivalIsBeforeDeparture = MomentHelper.isBefore(flight.getScheduledArrival(), flight.getScheduledDeparture());

			super.state(context, !arrivalIsBeforeDeparture, "scheduledArrival", "acme.validation.flight.arrival-before-departure.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
