
package acme.entities.maintenanceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public abstract class MaintenanceRecord extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	//TODO revisar ValidMoment
	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	private Date					maintenanceTimestamp;

	@Mandatory
	@Automapped
	@Valid
	private MaintenanceRecordStatus	status;

	//TODO revisar ValidMoment
	@Mandatory
	@Automapped
	@Temporal(TemporalType.DATE)
	private Date					nextInspectionDate;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money					estimatedCost;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String					notes;

}
