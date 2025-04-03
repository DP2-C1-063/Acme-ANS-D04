
package acme.features.flightCrewMembers.activityLogs;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
		boolean status;
		int masterId;
		int memberId;
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightAssignment assignment;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findAssignmentById(masterId);
		status = assignment.getFlightCrewMember().getId() == memberId && MomentHelper.isBefore(assignment.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log;
		int masterId;
		masterId = super.getRequest().getData("masterId", int.class);
		log = new ActivityLog();
		log.setDescription("");
		log.setFlightAssignment(this.repository.findAssignmentById(masterId));
		log.setIncidentType("");
		log.setRegistrationMoment(MomentHelper.getCurrentMoment());
		log.setSeverityLevel(0);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {

		super.bindObject(log, "incidentType", "description", "severityLevel");

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
		Dataset dataset;

		dataset = super.unbindObject(log, "incidentType", "description", "severityLevel");
		dataset.put("flightAssignment", log.getFlightAssignment());
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}

}
