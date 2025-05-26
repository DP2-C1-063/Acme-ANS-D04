
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
		if (!super.getRequest().getMethod().equals("POST"))
			super.getResponse().setAuthorised(false);
		else {

			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int bookingId = super.getRequest().getData("id", int.class);
			Booking booking = this.repository.getBookingById(bookingId);
			Integer flightId = super.getRequest().getData("flight", Integer.class);

			if (flightId != 0) {
				Flight flight = this.repository.getFlightById(flightId);
				status = status && flight != null;
			}
			if (super.getRequest().getMethod().equals("POST")) {
				int id;
				id = super.getRequest().getData("id", int.class);
				booking = this.repository.getBookingById(id);
				if (booking == null)
					status = false;
			}
			status = status && customerId == booking.getCustomer().getId();
			super.getResponse().setAuthorised(status);
		}
	}

	@Override
	public void load() {

		int id = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.getBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "travelClass", "price", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		Booking existing = this.repository.findBookingByLocator(booking.getLocatorCode());
		boolean valid = existing == null || existing.getId() == booking.getId();
		super.state(valid, "locatorCode", "acme.validation.booking.duplicated-locatorcode.message");

		Collection<BookingRecord> bookingRecords = this.repository.getAllBookingRecordsByBookingId(booking.getId());
		valid = !bookingRecords.isEmpty();
		super.state(valid, "*", "acme.validation.booking.noPassengers");

		boolean valid2 = bookingRecords.stream().filter(br -> br.getPassenger().isDraftMode()).findFirst().isEmpty();
		super.state(valid2, "*", "acme.validation.booking.passengerNotPublished");

		if (booking.getFlight() != null) {
			boolean valid3 = booking.getFlight().isPublished();
			super.state(valid3, "flight", "acme.validation.booking.flight.message");
		}
		boolean valid4 = booking.getLastNibble() != null && !booking.getLastNibble().isBlank();
		super.state(valid4, "lastNibble", "acme.validation.booking.NolastNibble");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices travelClasses;

		travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Flight> flights = this.repository.findAllFlights();
		SelectChoices flightChoices = SelectChoices.from(flights, "tag", booking.getFlight());

		dataset = super.unbindObject(booking, "flight", "locatorCode", "travelClass", "price", "lastNibble", "draftMode", "id");
		dataset.put("travelClasses", travelClasses);
		dataset.put("flights", flightChoices);
		super.getResponse().addData(dataset);
	}

}
