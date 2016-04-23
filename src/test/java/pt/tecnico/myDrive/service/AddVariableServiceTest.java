package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import pt.tecnico.myDrive.domain.EnvironmentVariable;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.InvalidEnvironmentVarNameException;

public class AddVariableServiceTest  extends TokenValidationServiceTest {
	Long token;
	String existantName = "ExistantName";
	String existantValue = "ExistantValue";
	String validString = "ValidString";
	
	@Override
	protected void populate() {
		super.populate();
		Login rootLogin = new Login("root", "***");
		token = rootLogin.getToken();
		
		new EnvironmentVariable (rootLogin, existantName, existantValue);
	}
	

	@Test(expected = InvalidEnvironmentVarNameException.class)
	public void nullName() {
		AddVariableService service = new AddVariableService(token, null, validString);
		service.execute();
	}
	
}
