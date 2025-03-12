
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPromotionCode;
import acme.constraints.ValidService;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidService
public class Service extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				linkPicture;

	@Mandatory
	@Automapped
	@ValidNumber
	private Double				dwellTime;

	@Optional
	@ValidPromotionCode
	@Column(unique = true)
	private String				promotionCode;

	@Optional
	@Automapped
	@Valid
	private Money				discount;

}
