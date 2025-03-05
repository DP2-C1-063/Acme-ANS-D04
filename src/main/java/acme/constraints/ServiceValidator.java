
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.service.Service;
import acme.entities.service.ServiceRepository;

public class ServiceValidator extends AbstractValidator<ValidService, Service> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ServiceRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidService annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (service == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean uniqueService;
			Service existingService;

			existingService = this.repository.findServiceByPromotionCode(service.getPromotionCode());
			uniqueService = existingService == null || existingService.equals(service);

			super.state(context, uniqueService, "PromotionCode", "acme.validation.service.duplicated-promotioncode.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
