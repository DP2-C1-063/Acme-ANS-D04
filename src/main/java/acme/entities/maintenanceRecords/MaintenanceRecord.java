
package acme.entities.maintenanceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public abstract class MaintenanceRecord extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	private Date					maintenanceTimestamp;

	@Mandatory
	@Automapped
	@Valid
	private MaintenanceRecordStatus	status;

	@Mandatory
	@Automapped
	private Date					nextInspectionDate;
}
