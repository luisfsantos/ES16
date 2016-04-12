package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.Link;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.EmptyUsernameException;
import pt.tecnico.myDrive.exception.InvalidUsernameOrPasswordException;
import pt.tecnico.myDrive.exception.UserDoesNotExistException;

public class LoginServiceTest extends AbstractServiceTest {


	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
		new User(manager, "Existent");
	}

	@Test
	public void successUserLogin() {
		LoginService service = new LoginService("Existent", "Existent");
		service.execute();

		Manager manager = Manager.getInstance();
		long token = service.result();
		User user = manager.getLoginByToken(token).getCurrentUser();

		assertThat("LoginToken is not a long", token, instanceOf(long.class));
		assertEquals("User from Token does not match", user.getName(), "Existent");
	}

	@Test
	public void successRootLogin() {
		LoginService service = new LoginService("Existent", "Existent");
		service.execute();

		Manager manager = Manager.getInstance();
		long token = service.result();
		User user = manager.getLoginByToken(token).getCurrentUser();

		assertThat("LoginToken is not a long", token, instanceOf(long.class));
		assertEquals("User from Token does not match", user.getName(), "root");

	}


}
