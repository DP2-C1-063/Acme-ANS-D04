
package acme.entities.service;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPromotionCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service {

	/*
	 * 7) A service is an offering provided by airports to enhance a passenger’s experience.
	 * The system must store the following data about them:
	 * a name (up to 50 characters),
	 * a link to a picture that must be stored somewhere else,
	 * and its average dwell time (in hours);
	 * optionally, it can store a promotion code (unique, "^[A-Z]{4}-[0-9]{2}$", the last two digits correspond to the current year),
	 * and the money that should be discounted when the promotion code is applied.
	 */

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String	name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String	linkPicture;

	@Mandatory
	@Automapped
	@ValidNumber
	private Double	dwellTime;

	@Optional
	@Automapped
	@ValidPromotionCode
	private String	promotionCode;

	@Optional
	@Automapped
	@Valid
	private Money	discount;

}
