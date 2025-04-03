
package acme.features.technician.taskInvolvesRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecords.MaintenanceRecord;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskInvolvesRecord;

public interface TechnicianTaskInvolvesRecordRepository extends AbstractRepository {

	@Query("select i from TaskInvolvesRecord i where i.maintenanceRecord.id = :id")
	Collection<TaskInvolvesRecord> findTaskInvolvedByRecord(int id);

	@Query("SELECT m FROM MaintenanceRecord m WHERE m.id = :id")
	MaintenanceRecord findMaintenanceRecordById(final int id);
	@Query("SELECT m FROM Task m WHERE m.id = :id")
	Task findTaskById(final int id);

	@Query("SELECT i FROM TaskInvolvesRecord i WHERE i.maintenanceRecord.id = :id")
	Collection<TaskInvolvesRecord> findAllTaskAssociatedWith(final int id);

	@Query("SELECT i FROM TaskInvolvesRecord i WHERE i.id = :id")
	TaskInvolvesRecord findTaskInvolvesRecordById(final int id);

	@Query("SELECT t FROM Task t")
	Collection<Task> findAllTasks();

}
