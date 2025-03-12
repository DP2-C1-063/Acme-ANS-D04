
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.realms.assistanceAgent.AssistanceAgent;
import acme.realms.assistanceAgent.AssistanceAgentRepository;

public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Autowired
	private AssistanceAgentRepository assistanceAgentRepository;


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (assistanceAgent == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {

			boolean uniqueAssistanceAgent;
			AssistanceAgent existingAssistanceAgent;

			existingAssistanceAgent = this.assistanceAgentRepository.findByEmployeeCode(assistanceAgent.getEmployeeCode()).get(0);
			uniqueAssistanceAgent = existingAssistanceAgent == null || existingAssistanceAgent.equals(assistanceAgent) || this.assistanceAgentRepository.findByEmployeeCode(assistanceAgent.getEmployeeCode()).size() < 2;

			super.state(context, uniqueAssistanceAgent, "employeeCode", "acme.validation.assistanceagent.duplicated-employeecode.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
