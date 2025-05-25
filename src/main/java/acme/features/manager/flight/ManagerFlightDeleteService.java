
package acme.features.manager.flight;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.claim.Claim;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.manager.Manager;

@GuiService
public class ManagerFlightDeleteService extends AbstractGuiService<Manager, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		if (super.getRequest().getMethod().equals("POST")) {
			int masterId;
			Flight flight;
			Manager manager;

			masterId = super.getRequest().getData("id", int.class);
			flight = this.repository.findFlightById(masterId);
			manager = flight == null ? null : flight.getManager();
			status = flight != null && super.getRequest().getPrincipal().hasRealm(manager) && !flight.isPublished();
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "indication", "cost", "description");
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		Collection<Leg> legs;
		Collection<TrackingLog> trackingLogs;
		Collection<Claim> claims;
		Collection<FlightAssignment> flightAssignments;
		Collection<Booking> bookings;
		Collection<BookingRecord> bookingRecords;

		legs = this.repository.findLegsByFlightId(flight.getId());
		bookings = this.repository.findAllBookingsByFlightId(flight.getId());
		trackingLogs = new ArrayList<>();
		claims = new ArrayList<>();
		flightAssignments = new ArrayList<>();
		bookingRecords = new ArrayList<>();

		for (Leg l : legs) {
			claims.addAll(this.repository.findAllClaimsByLegId(l.getId()));
			flightAssignments.addAll(this.repository.findAllFlightAssignmentsByLegId(l.getId()));
		}
		for (Claim c : claims)
			trackingLogs.addAll(this.repository.findAllTrackingLogsByClaimId(c.getId()));
		for (Booking b : bookings)
			bookingRecords.addAll(this.repository.findAllBookingRecordsByBookingId(b.getId()));

		this.repository.deleteAll(trackingLogs);
		this.repository.deleteAll(claims);
		this.repository.deleteAll(flightAssignments);
		this.repository.deleteAll(bookingRecords);
		this.repository.deleteAll(bookings);
		this.repository.deleteAll(legs);
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "description", "published");

		super.getResponse().addData(dataset);
	}
}
