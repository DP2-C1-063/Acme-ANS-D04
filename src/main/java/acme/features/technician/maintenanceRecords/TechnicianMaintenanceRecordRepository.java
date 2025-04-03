
package acme.features.technician.maintenanceRecords;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.tasks.TaskInvolvesRecord;

public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("SELECT m FROM MaintenanceRecord m WHERE m.technician.id= :id")
	Collection<MaintenanceRecord> findMaintenanceRecordsOfTechnician(final int id);

	@Query("SELECT m FROM MaintenanceRecord m WHERE m.id= :id")
	MaintenanceRecord findMaintenanceRecordById(final int id);

	@Query("SELECT a FROM Aircraft a WHERE a.id = :id")
	Aircraft findAircraftById(final int id);

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircraft();

	@Query("SELECT i.task FROM TaskInvolvesRecord i WHERE i.id = :id")
	Collection<TaskInvolvesRecord> findAllTaskAssociatedWith(final int id);
}
