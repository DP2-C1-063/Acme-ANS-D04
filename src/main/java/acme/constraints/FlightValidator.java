
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	LegRepository		legRepository;

	@Autowired
	FlightRepository	repository;

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
		else if (flight.isPublished()) {
			List<Leg> legs;
			legs = this.legRepository.findLegsByFlight(flight.getId());
			{
				boolean flightHasAtLeastOneLeg;

				flightHasAtLeastOneLeg = legs != null && !legs.isEmpty();

				super.state(context, flightHasAtLeastOneLeg, "*", "acme.validation.flight.legs.message");
			}
			{
				boolean legsOverlap = false;

				if (legs != null && legs.size() > 1)
					for (int i = 0; i < legs.size() - 1; i++) {
						Leg actualLeg = legs.get(i);
						Leg nextLeg = legs.get(i + 1);

						if (MomentHelper.isAfter(actualLeg.getScheduledArrival(), nextLeg.getScheduledDeparture())) {
							legsOverlap = true;
							break;
						}
					}
				super.state(context, !legsOverlap, "*", "acme.validation.flight.legs-overlap.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
