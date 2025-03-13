
package acme.realms.technicians;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLicenseNumber;
import acme.constraints.ValidPhoneNumber;
import acme.constraints.ValidTechnician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTechnician
public class Technician extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidLicenseNumber
	private String				licenseNumber;

	@Mandatory
	@Automapped
	@ValidPhoneNumber
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				specialisation;

	@Mandatory
	@Automapped
	private Boolean				healthTestPassed;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 120)
	private Integer				experienceYears;

	@Optional
	@ValidString(min = 1, max = 255)
	private String				certifications;
}
