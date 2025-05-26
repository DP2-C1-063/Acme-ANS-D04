
package acme.features.flightCrewMembers.activityLogs;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.features.flightCrewMembers.flightAssignments.FlightCrewMemberFlightAssignmentRepository;
import acme.realms.flightCrewMembers.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository		repository;

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository	assignmentRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		ActivityLog log;
		int id;
		int memberId;
		boolean method = true;
		boolean completed = false;
		if (super.getRequest().getMethod().equals("GET"))
			method = false;
		else {
			id = super.getRequest().getData("id", int.class);
			log = this.repository.findActivityLogById(id);

			memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

			if (log != null) {

				status = log.getFlightAssignment().getFlightCrewMember().getId() == memberId && log.isDraftMode();
				completed = MomentHelper.isBefore(log.getFlightAssignment().getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
			}
		}
		super.getResponse().setAuthorised(status && completed && method);

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

		super.bindObject(log, "incidentType", "description", "severityLevel");

	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.delete(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "incidentType", "description", "severityLevel", "draftMode");
		dataset.put("flightAssignment", log.getFlightAssignment().getId());

		super.getResponse().addData(dataset);
	}

}
