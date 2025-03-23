
package acme.features.manager.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.leg.Leg;
import acme.realms.manager.Manager;

@GuiController
public class ManagerLegController extends AbstractGuiController<Manager, Leg> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerLegListService listService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}
}
