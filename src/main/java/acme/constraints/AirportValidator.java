
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;

public class AirportValidator extends AbstractValidator<ValidAirport, Airport> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AirportRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidAirport annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airport airport, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (airport == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean uniqueAirport;
			Airport existingAirport;

			existingAirport = this.repository.findAirportByIATACode(airport.getIATACode());
			uniqueAirport = existingAirport == null || existingAirport.equals(airport);

			super.state(context, uniqueAirport, "IATACode", "acme.validation.airport.duplicated-IATA.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
