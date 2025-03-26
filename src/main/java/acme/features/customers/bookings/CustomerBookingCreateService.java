
package acme.features.customers.bookings;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import acme.entities.passenger.Passenger;
import acme.entities.passenger.PassengerRepository;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository	repository;

	@Autowired
	private FlightRepository			flightRepository;

	@Autowired
	private PassengerRepository			passengerRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking;

		booking = new Booking();
		booking.setLocatorCode(null);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setTravelClass(null);
		booking.setLastNibble("");
		booking.setCustomer(null);
		booking.setFlight(null);
		booking.setDraftMode(true);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		SelectChoices flightChoices;
		SelectChoices passengerChoices;
		int customerId;
		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Dataset dataset;
		Collection<Flight> flights = this.flightRepository.findAllFlightsByBooking(booking.getId());
		Collection<Passenger> passengers = this.passengerRepository.findAllPassengersByBooking(booking.getId());
		flightChoices = SelectChoices.from(flights, "id", null);
		passengerChoices = SelectChoices.from(passengers, "id", null);

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);
	}
}
