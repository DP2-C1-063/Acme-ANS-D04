
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
	@ValidLicenseNumber
	@Column(unique = true)
	private String				licenseNumber;

	@Mandatory
	@ValidPhoneNumber
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				specialisation;

	@Mandatory
	@Automapped
	private boolean				healthTestPassed;

	@Mandatory
	@ValidNumber(min = 0, max = 120)
	@Automapped
	private int					experienceYears;

	@Optional
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				certifications;
}
