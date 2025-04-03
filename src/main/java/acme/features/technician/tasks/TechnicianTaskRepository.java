
package acme.features.technician.tasks;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.tasks.Task;
import acme.entities.tasks.TaskInvolvesRecord;
import acme.realms.technician.Technician;

public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select t from Technician t where t.id = :id")
	Technician findTechnicianById(int id);

	@Query("select t from Task t where t.technician.id = :id")
	Collection<Task> findTasksOfTechnician(final int id);

	@Query("select i from TaskInvolvesRecord i where i.task.id = :id")
	Collection<TaskInvolvesRecord> findAllInvolvesAssociatedWith(final int id);
}
