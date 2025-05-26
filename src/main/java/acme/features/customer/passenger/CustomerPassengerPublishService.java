
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerPassengerPublishService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
		if (!super.getRequest().getMethod().equals("POST"))
			super.getResponse().setAuthorised(false);
		else {

			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int passengerId = super.getRequest().getData("id", int.class);
			Passenger passenger = this.repository.getPassengerById(passengerId);

			if (super.getRequest().getMethod().equals("POST")) {
				int id;
				id = super.getRequest().getData("id", int.class);
				passenger = this.repository.getPassengerById(id);
				if (passenger == null)
					status = false;
			}
			status = status && customerId == passenger.getCustomer().getId();
			super.getResponse().setAuthorised(status);
		}
	}

	@Override
	public void load() {

		int id = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.getPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "name", "email", "passport", "birth", "needs");
	}

	@Override
	public void validate(final Passenger passenger) {

	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(false);
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "name", "email", "passport", "birth", "needs", "draftMode");

		super.getResponse().addData(dataset);
	}

}
