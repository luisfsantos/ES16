package pt.tecnico.myDrive.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import pt.tecnico.myDrive.domain.EnvironmentVariable;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.exception.InvalidEnvironmentVarNameException;
import pt.tecnico.myDrive.exception.InvalidEnvironmentVarValueException;
import pt.tecnico.myDrive.service.dto.VariableDto;

public class AddVariableServiceTest  extends TokenValidationServiceTest {
	Long token;
	String existantName = "ExistantName";
	String existantValue = "ExistantValue";
	String inexistantName = "InexistantName";
	String validString = "ValidString";
	
	@Override
	protected void populate() {
		super.populate();
		Login rootLogin = new Login("root", "***");
		token = rootLogin.getToken();
		
		new EnvironmentVariable (rootLogin, existantName, existantValue);
	}
	
	// test 3
	@Test(expected = InvalidEnvironmentVarNameException.class)
	public void nullName() {
		AddVariableService service = new AddVariableService(token, null, validString);
		service.execute();
	}
	
	// test 4
	@Test(expected = InvalidEnvironmentVarValueException.class)
	public void nullValue() {
		AddVariableService service = new AddVariableService(token, inexistantName, null);
		service.execute();
	}
	
	// test 5
	@Test
	public void emptyString() {
		AddVariableService service = new AddVariableService(token, inexistantName, "");
		service.execute();
		List<VariableDto> res = service.result();
		assertEquals("Invalid number of variables", 2, res.size());
		int indexExistant = 0;
		int count = 0;
		for (VariableDto var: res) {
			if (var.getName().equals(existantName)){
				indexExistant = count;
			}
			count++;
		}
		assertEquals("Invalid existant name", existantName, res.get(indexExistant).getName());
		assertEquals("Invalid inexistant name", inexistantName, res.get(1 - indexExistant).getName());
		
		assertEquals("Invalid existant value", existantValue, res.get(indexExistant).getValue());
		assertEquals("Invalid inexistant value", "", res.get(1 - indexExistant).getValue());
	}
	
	// test 6
	@Test
	public void validString() {
		AddVariableService service = new AddVariableService(token, inexistantName, validString);
		service.execute();
		List<VariableDto> res = service.result();
		assertEquals("Invalid number of variables", 2, res.size());
		
		int indexExistant = 0;
		int count = 0;
		for (VariableDto var: res) {
			if (var.getName().equals(existantName)){
				indexExistant = count;
			}
			count++;
		}
		assertEquals("Invalid existant name", existantName, res.get(indexExistant).getName());
		assertEquals("Invalid inexistant name", inexistantName, res.get(1 - indexExistant).getName());
		
		assertEquals("Invalid existant value", existantValue, res.get(indexExistant).getValue());
		assertEquals("Invalid inexistant value", validString, res.get(1 - indexExistant).getValue());
		
	}
	
	// test 7
	@Test
	public void existantName() {
		AddVariableService service = new AddVariableService(token, existantName, validString);
		service.execute();
		List<VariableDto> res = service.result();
		assertEquals("Invalid number of variables", 1, res.size());
		assertEquals("Invalid existant name", existantName, res.get(0).getName());
		assertEquals("Invalid existant value", validString, res.get(0).getValue());
	}
	
}
