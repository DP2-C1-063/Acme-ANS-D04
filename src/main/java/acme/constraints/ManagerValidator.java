
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.realms.manager.Manager;
import acme.realms.manager.ManagerRepository;

public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean uniqueManager;
			Manager existingManager;

			existingManager = this.repository.findManagerByIdentifierNumber(manager.getIdentifierNumber());
			uniqueManager = existingManager == null || existingManager.equals(manager);

			super.state(context, uniqueManager, "identifierNumber", "acme.validation.manager.duplicated-identifier-number.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
