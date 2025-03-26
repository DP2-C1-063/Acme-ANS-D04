
package acme.realms.assistanceAgent;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidEmployeeCode;
import acme.entities.airlines.Airlines;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//@ValidAssistanceAgent
public class AssistanceAgent extends AbstractRole {

	private static final long	serialVersionUID	= 1L;
	@Mandatory
	@Automapped
	@ValidEmployeeCode
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				spokenLanguages;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				momentBeginningWorking;

	@Optional
	@Automapped
	@ValidString(min = 0, max = 255)
	private String				bio;

	@Optional
	@ValidMoney(min = 0.00, max = 1000000)
	@Automapped
	private Money				salary;

	@Optional
	@ValidUrl
	@Automapped
	private String				photo;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airlines			airline;

}
