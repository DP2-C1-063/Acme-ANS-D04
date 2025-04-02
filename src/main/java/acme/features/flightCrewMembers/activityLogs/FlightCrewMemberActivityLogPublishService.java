
package acme.features.flightCrewMembers.activityLogs;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.features.flightCrewMembers.flightAssignments.FlightCrewMemberFlightAssignmentRepository;
import acme.realms.flightCrewMembers.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository		repository;

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository	assignmentRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		ActivityLog log;
		int id;
		int memberId;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(id);

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = log.getFlightAssignment().getFlightCrewMember().getId() == memberId && log.isDraftMode();
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		ActivityLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(id);

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
		boolean assignmentPublished = !log.getFlightAssignment().isDraftMode();
		super.state(assignmentPublished, "*", "flight-crew-member.activity-log.assignment-not-published-yet");
	}

	@Override
	public void perform(final ActivityLog log) {

		log.setDraftMode(false);
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "incidentType", "description", "severityLevel", "draftMode");
		dataset.put("flightAssignment", log.getFlightAssignment().getId());

		super.getResponse().addData(dataset);
	}

}
