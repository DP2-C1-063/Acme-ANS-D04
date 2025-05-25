
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		boolean isAuthorised = false;

		if (super.getRequest().getPrincipal().hasRealmOfType(Customer.class)) {
			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int masterId = super.getRequest().getData("masterId", int.class);

			Booking booking = this.repository.getBookingById(masterId);

			if (booking != null) {
				boolean isCustomer = booking.getCustomer().getId() == customerId;

				isAuthorised = isCustomer;

			}
		}

		super.getResponse().setAuthorised(isAuthorised);

	}

	@Override
	public void load() {
		int masterId = super.getRequest().getData("masterId", int.class);
		Booking booking = this.repository.getBookingById(masterId);
		BookingRecord bookingRecord = new BookingRecord();
		bookingRecord.setBooking(booking);
		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		if (bookingRecord.getPassenger() != null) {
			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Collection<Passenger> includedPassengers = this.repository.getPassengersInBooking(bookingRecord.getBooking().getId());
			boolean valid = bookingRecord.getPassenger().getCustomer().getId() == customerId && !includedPassengers.contains(bookingRecord.getPassenger());
			super.state(valid, "passenger", "acme.validation.booking.doublePassenger");
		}

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		assert bookingRecord != null;
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord, "passenger", "booking");
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int masterId = super.getRequest().getData("masterId", int.class);
		Collection<Passenger> addedPassengers = this.repository.getPassengersInBooking(masterId);

		Collection<Passenger> passengers = this.repository.getAllPassengersOf(customerId).stream().filter(p -> !addedPassengers.contains(p)).toList();
		SelectChoices passengerChoices = SelectChoices.from(passengers, "name", bookingRecord.getPassenger());
		dataset.put("locatorCode", bookingRecord.getBooking().getLocatorCode());
		dataset.put("passengers", passengerChoices);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);

	}

}
