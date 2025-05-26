
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegDeleteService extends AbstractGuiService<Manager, Leg> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		if (super.getRequest().getMethod().equals("POST")) {
			Flight flight;
			Manager manager;
			int legId;
			Leg leg;

			legId = super.getRequest().getData("id", int.class);
			leg = this.repository.findLegById(legId);

			if (leg != null && !leg.isPublished()) {
				flight = leg.getFlight();
				if (flight != null) {
					manager = flight.getManager();
					status = super.getRequest().getPrincipal().hasRealm(manager);
				} else
					status = false;
			} else
				status = false;
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "number", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft");
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		Collection<FlightAssignment> flightAssignments;
		Collection<Claim> claims;
		Collection<TrackingLog> trackingLogs;

		flightAssignments = this.repository.findAllFlightAssignmentsByLegId(leg.getId());
		claims = this.repository.findAllClaimsByLegId(leg.getId());
		trackingLogs = this.repository.findAllTrackingLogsByClaimId(leg.getId());

		this.repository.deleteAll(trackingLogs);
		this.repository.deleteAll(claims);
		this.repository.deleteAll(flightAssignments);
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "number", "scheduledDeparture", "scheduledArrival", "status", "departureAirport", "arrivalAirport", "aircraft");

		super.getResponse().addData(dataset);
	}

}
