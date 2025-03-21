
package acme.features.administrator.airlines;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.airlines.Airlines;

@GuiController
public class AdministratorAirlinesController extends AbstractGuiController<Administrator, Airlines> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlinesListService	listService;

	@Autowired
	private AdministratorAirlinesShowService	showService;

	@Autowired
	private AdministratorAirlinesCreateService	createService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
	}

}
