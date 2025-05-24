
package acme.features.flightCrewMembers.activityLogs;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.flightCrewMembers.FlightCrewMembers;

@GuiService
public class FlightCrewMemberActivityLogShowService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int memberId;
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		ActivityLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findActivityLogById(id);

		status = log.getFlightAssignment().getFlightCrewMember().getId() == memberId;
		boolean completed = MomentHelper.isBefore(log.getFlightAssignment().getLeg().getScheduledArrival(), MomentHelper.getCurrentMoment());
		super.getResponse().setAuthorised(status && completed);
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
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severityLevel", "draftMode");
		dataset.put("flightAssignment", log.getFlightAssignment().getId());
		super.getResponse().addData(dataset);
	}

}
