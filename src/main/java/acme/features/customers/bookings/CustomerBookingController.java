
package acme.features.customers.bookings;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.entities.booking.Booking;
import acme.realms.customer.Customer;

public class CustomerBookingController extends AbstractGuiController<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingListService	listService;

	@Autowired
	private CustomerBookingShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("list", this.listService);

		super.addBasicCommand("show", this.showService);
	}

}
