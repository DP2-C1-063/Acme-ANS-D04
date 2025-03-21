
package acme.features.administrator.airlines;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airlines;

@GuiService
public class AdministratorAirlinesCreateService extends AbstractGuiService<Administrator, Airlines> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlinesRepository repository;

	// AbstractGuiService interface -------------------------------------------

}
