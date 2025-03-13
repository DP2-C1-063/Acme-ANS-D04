
package acme.entities.trackingLogs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.entities.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;
	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				lastUpdateMoment;
	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				step;
	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;
	@Mandatory
	@Valid
	@Automapped
	private TrackingLogStatus	status;
	@Optional
	@Automapped
	@ValidString(min = 0, max = 255)
	private String				resolution;
	@Mandatory
	@ManyToOne
	private Claim				claim;
}
