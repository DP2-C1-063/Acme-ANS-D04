
package acme.entities.assistanceAgent;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidAssistanceAgent;
import acme.constraints.ValidEmployeeCode;
import acme.entities.airlines.Airlines;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAssistanceAgent
public abstract class AssistanceAgent extends AbstractEntity {

	@Mandatory
	@Automapped
	@ValidEmployeeCode
	private String		employeeCode;
	@Mandatory
	@Automapped
	@Length(min = 1, max = 255)
	private String		spokenLanguages;
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airlines	airline;
	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date		momentBeginningWorking;
	@Optional
	@Automapped
	@Length(min = 1, max = 255)
	private String		bio;
	@Optional
	@ValidMoney
	@Automapped
	private Money		salary;
	@Optional
	@ValidUrl
	@Automapped
	private String		photo;

}
