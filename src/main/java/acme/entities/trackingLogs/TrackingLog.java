
package acme.entities.trackingLogs;

import java.util.Date;

import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.entities.claim.Claim;

public class TrackingLog {

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date	lastUpdateMoment;
	@Mandatory
	@Automapped
	@Length(min = 1, max = 50)
	private String	step;
	@Mandatory
	@ValidScore
	@Range(min = 0, max = 100)
	@Automapped
	private Double	resolutionPercentage;
	@Mandatory
	@Automapped
	private boolean	accepted;
	@Mandatory
	@Automapped
	@Length(min = 1, max = 255)
	private String	resolution;
	@Mandatory
	@ManyToOne
	private Claim	claim;
}
