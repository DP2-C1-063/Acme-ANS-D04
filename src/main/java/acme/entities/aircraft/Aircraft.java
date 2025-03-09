
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
import acme.constraints.ValidAircraft;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAircraft
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;
	@Mandatory
	@Automapped
	@Length(min = 1, max = 50)
	private String				model;
	@Mandatory
	@Length(min = 1, max = 50)
	@Column(unique = true)
	private String				registrationNumber;
	@Mandatory
	@Range(min = 1, max = 255)
	@Automapped
	private Integer				capacity;
	@Mandatory
	@Range(min = 20000, max = 50000)
	@Automapped
	private Integer				cargoWeight;
	@Mandatory
	@Valid
	@Automapped
	private AircraftStatus		status;
	@Optional
	@Automapped
	@Length(min = 0, max = 255)
	private String				details;
}
