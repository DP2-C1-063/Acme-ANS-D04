
package acme.entities.maintenanceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidMaintenanceRecord;
import acme.entities.aircraft.Aircraft;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidMaintenanceRecord
public class MaintenanceRecord extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date					maintenanceMoment;

	@Mandatory
	@Automapped
	@Valid
	private MaintenanceRecordStatus	status;

	@Mandatory
	@Temporal(TemporalType.DATE)
	@ValidMoment(past = false)
	private Date					nextInspection;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money					estimatedCost;

	@Optional
	@Automapped
	@ValidString(min = 0, max = 255)
	private String					notes;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician				technician;

	@Mandatory
	@ManyToOne(optional = false)

	private Aircraft				relatedAircreft;

}
