
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public abstract class Aircraft extends AbstractEntity {

	@Mandatory
	@Automapped
	@Length(min = 1, max = 50)
	private String			model;
	@Mandatory
	@Length(min = 1, max = 50)
	@Column(unique = true)
	private String			registrationNumber;
	@Mandatory
	@Range(min = 2, max = 5)
	@Automapped
	private Integer			cargoWeight;
	@Mandatory
	@Valid
	@Automapped
	private AircraftStatus	status;
	@Optional
	@Automapped
	@Length(min = 1, max = 255)
	private String			details;
}
