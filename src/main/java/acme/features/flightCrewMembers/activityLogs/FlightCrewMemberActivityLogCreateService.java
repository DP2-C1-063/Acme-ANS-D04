
package acme.features.flightCrewMembers.activityLogs;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.features.flightCrewMembers.flightAssignments.FlightCrewMemberFlightAssignmentRepository;
import acme.realms.flightCrewMembers.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository		repository;

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository	assignmentRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ActivityLog log;

		log = new ActivityLog();
		log.setDescription("");
		log.setFlightAssignment(null);
		log.setIncidentType("");
		log.setRegistrationMoment(MomentHelper.getCurrentMoment());
		log.setSeverityLevel(0);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		int assignmentId;
		FlightAssignment assignment;
		super.bindObject(log, "incidentType", "description", "severityLevel");
		assignmentId = super.getRequest().getData("flightAssignment", int.class);
		assignment = this.assignmentRepository.findAssignmentById(assignmentId);
		log.setFlightAssignment(assignment);

	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		SelectChoices choices;
		int memberId;
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Dataset dataset;
		Date currentMoment = MomentHelper.getCurrentMoment();
		Collection<FlightAssignment> assignments = this.assignmentRepository.findAllCompletedAssignmentsOfCrewMember(memberId, currentMoment);
		choices = SelectChoices.from(assignments, "id", null);

		dataset = super.unbindObject(log, "incidentType", "description", "severityLevel");
		dataset.put("flightAssignment", choices.getSelected().getKey());
		dataset.put("assignments", choices);

		super.getResponse().addData(dataset);
	}

}
