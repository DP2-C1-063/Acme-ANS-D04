
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().getMethod().equals("POST")) {
			int id = super.getRequest().getData("id", int.class);
			status = id == 0;
		}

		if (super.getRequest().hasData("flight")) {
			int flightId = super.getRequest().getData("flight", int.class);
			if (flightId != 0) {
				Flight flight = this.repository.getFlightById(flightId);
				status = status && flight != null;
			}
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		Booking booking;

		booking = new Booking();
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setDraftMode(true);
		booking.setCustomer(customer);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "flight", "locatorCode", "travelClass", "price", "lastNibble");
	}

	@Override
	public void validate(final Booking booking) {
		Booking existing = this.repository.findBookingByLocator(booking.getLocatorCode());
		boolean valid = existing == null;
		super.state(valid, "locatorCode", "acme.validation.booking.locatorCode.message");
		if (booking.getFlight() != null) {
			boolean valid2 = booking.getFlight().isPublished();
			super.state(valid2, "flight", "acme.validation.booking.flight.message");
		}

	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(true);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		// assert booking != null;
		Dataset dataset;
		SelectChoices travelClasses = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		Collection<Flight> flights = this.repository.findAllFlights();
		dataset = super.unbindObject(booking, "flight", "locatorCode", "travelClass", "lastNibble", "draftMode", "id");
		dataset.put("travelClasses", travelClasses);

		SelectChoices flightChoices = SelectChoices.from(flights, "tag", booking.getFlight());
		dataset.put("flights", flightChoices);

		super.getResponse().addData(dataset);

	}
}
