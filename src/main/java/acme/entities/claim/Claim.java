
package acme.entities.claim;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ManyToOne
	private AssistanceAgent		assistanceAgent;
	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				registrationMoment;
	@Mandatory
	@ValidEmail
	@Length(min = 1, max = 255)
	@Automapped
	private String				passengerEmail;
	@Mandatory
	@Automapped
	@Length(min = 1, max = 255)
	private String				description;
	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;
	@Mandatory
	@Automapped
	private boolean				accepted;
	@Mandatory

	@Automapped
	private Leg					leg;

}
