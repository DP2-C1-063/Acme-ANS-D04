
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.airlines.AirlineRepository;
import acme.entities.airlines.Airlines;

public class AirlineValidator extends AbstractValidator<ValidAirline, Airlines> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidAirline annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airlines airline, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (airline == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean uniqueAirline;
			Airlines existingAirline;

			existingAirline = this.repository.findAirlineByIATA(airline.getIATACode());
			uniqueAirline = existingAirline == null || existingAirline.equals(airline);

			super.state(context, uniqueAirline, "IATACode", "acme.validation.airline.duplicated-IATA.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
