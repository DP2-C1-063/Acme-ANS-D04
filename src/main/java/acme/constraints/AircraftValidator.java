
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftRepository;

@Validator
public class AircraftValidator extends AbstractValidator<ValidAircraft, Aircraft> {

	@Autowired
	private AircraftRepository aircraftRepository;


	@Override
	protected void initialise(final ValidAircraft annotation) {
		assert annotation != null;
	}
	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (aircraft == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean uniqueAircraft;
			Aircraft existingAircraft;

			existingAircraft = this.aircraftRepository.findByRegistrationNumber(aircraft.getRegistrationNumber());
			uniqueAircraft = existingAircraft == null || existingAircraft.equals(aircraft);

			super.state(context, uniqueAircraft, "registrationNumber", "acme.validation.aircraft.duplicated-RegistrationNumber.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
