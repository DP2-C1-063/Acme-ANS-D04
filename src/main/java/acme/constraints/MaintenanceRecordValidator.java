
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.maintenanceRecords.MaintenanceRecord;

@Validator
public class MaintenanceRecordValidator extends AbstractValidator<ValidMaintenanceRecord, MaintenanceRecord> {

	@Override
	public boolean isValid(final MaintenanceRecord value, final ConstraintValidatorContext context) {
		boolean result;

		if (value == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean isBefore = MomentHelper.isBefore(value.getMaintenanceMoment(), value.getNextInspection());
			super.state(context, isBefore, "*", "acme.validation.maintenance-record.invalid-moment.message");
		}

		result = !super.hasErrors(context);

		return result;

	}

}
