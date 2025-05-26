
package acme.entities.claim;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.leg.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.realms.assistanceAgent.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "assistance_agent_id")

})

public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;
	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode			= true;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				review				= false;


	@Transient
	public String getStatus() {
		String result = "PENDING";
		TrackingLogRepository trackingLogRepository;
		List<TrackingLog> wrapper;

		trackingLogRepository = SpringHelper.getBean(TrackingLogRepository.class);

		wrapper = trackingLogRepository.getLastTrackingLogByClaim(this.getId());
		if (!wrapper.isEmpty() && wrapper != null) {
			TrackingLog trackingLog = wrapper.getFirst();
			if (trackingLog.getResolutionPercentage() == 100.0)
				result = trackingLog.getStatus().toString();
		}
		return result;
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg				leg;

	@Mandatory
	@Valid
	@ManyToOne
	private AssistanceAgent	assistanceAgent;
}
