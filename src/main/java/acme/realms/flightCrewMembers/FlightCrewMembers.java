
package acme.realms.flightCrewMembers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidEmployeeCode;
import acme.constraints.ValidFlightCrewMember;
import acme.constraints.ValidPhoneNumber;
import acme.entities.airlines.Airlines;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightCrewMember
public class FlightCrewMembers extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidEmployeeCode
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidPhoneNumber
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				languageSkills;

	@Mandatory
	@Valid
	@Automapped
	private AvailabilityStatus	availabilityStatus;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidNumber(min = 0, max = 120)
	@Automapped
	private Integer				yearsExperience;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airlines			airline;
}
