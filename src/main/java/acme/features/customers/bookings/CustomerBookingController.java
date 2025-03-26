
package acme.features.customers.bookings;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.entities.booking.Booking;
import acme.realms.customer.Customer;

public class CustomerBookingController extends AbstractGuiController<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingListService		listService;

	@Autowired
	private CustomerBookingShowService		showService;

	@Autowired
	private CustomerBookingsCreateService	createService;

	@Autowired
	private CustomerBookingsPublishService	publishService;

	@Autowired
	private CustomerBookingsUpdateService	updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("list", this.listService);

		super.addBasicCommand("show", this.showService);

		super.addBasicCommand("create", this.createService);

		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
