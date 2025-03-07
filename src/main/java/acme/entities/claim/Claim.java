
package acme.entities.claim;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.entities.assistanceAgent.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public abstract class Claim extends AbstractEntity {

	@Mandatory
	@OneToOne
	private AssistanceAgent	assistanceAgent;
	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date			registrationMoment;
	@Mandatory
	@ValidEmail
	@Automapped
	private String			passengerEmail;
	@Mandatory
	@Automapped
	@Length(min = 1, max = 255)
	private String			description;
	@Mandatory
	@Valid
	@Automapped
	private ClaimStatus		status;
	@Mandatory
	@Automapped
	private boolean			accepted;

}
