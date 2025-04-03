
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.realms.technician.Technician;
import acme.realms.technician.TechnicianRepository;

public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	@Autowired
	private TechnicianRepository repository;


	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (technician == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean uniqueLicenseNumber;
			Technician existingLicenseNumber;

			existingLicenseNumber = this.repository.findTechnicianByLicenseNumber(technician.getLicenseNumber());
			uniqueLicenseNumber = existingLicenseNumber == null || existingLicenseNumber.equals(technician);

			super.state(context, uniqueLicenseNumber, "LicenseNumber", "acme.validation.technician.duplicated-license-number.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
