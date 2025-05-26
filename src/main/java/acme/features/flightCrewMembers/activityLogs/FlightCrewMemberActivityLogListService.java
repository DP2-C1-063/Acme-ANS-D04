
package acme.features.flightCrewMembers.activityLogs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		int masterId;
		int memberId;
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightAssignment assignment;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findAssignmentById(masterId);
		if (assignment != null)
			status = assignment.getFlightCrewMember().getId() == memberId && MomentHelper.isBefore(assignment.getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ActivityLog> logs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		logs = this.repository.findAllActivityLogsOfAssignment(masterId);
		super.getResponse().addGlobal("masterId", masterId);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		int masterId;

		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "severityLevel");
		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);

		super.getResponse().addData(dataset);
	}

}
