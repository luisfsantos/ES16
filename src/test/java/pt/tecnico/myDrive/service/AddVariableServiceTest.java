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
	String existantName1 = "aexistantName";
	String existantName2 = "ZexistantName";
	String existantValue = "ExistantValue";
	String inexistantName = "InexistantName";
	String validString = "ValidString";
	
	@Override
	protected void populate() {
		super.populate();
		Login rootLogin = new Login("root", "***");
		token = rootLogin.getToken();
		
		new EnvironmentVariable (rootLogin, existantName2, existantValue);
		new EnvironmentVariable (rootLogin, existantName1, existantValue);
	}
	
	// test 3
	@Test(expected = InvalidEnvironmentVarNameException.class)
	public void nullName() {
		AddVariableService service = new AddVariableService(token, null, validString);
		service.execute();
	}
	
	
	// test 4
	@Test
	public void nullValueExistName() {
		AddVariableService service = new AddVariableService(token, existantName1, null);
		service.execute();
		
		List<VariableDto> res = service.result();
		assertEquals("Invalid number of variables", 2, res.size());
		
		assertEquals("Invalid existant name or order", existantName1, res.get(0).getName());
		assertEquals("Invalid existant value", existantValue, res.get(0).getValue());
		
		assertEquals("Invalid existant name or order", existantName2, res.get(1).getName());
		assertEquals("Invalid existant value", existantValue, res.get(1).getValue());
	}
	
	
	// test 5
	@Test(expected = InvalidEnvironmentVarValueException.class)
	public void nullValueInexistName() {
		AddVariableService service = new AddVariableService(token, inexistantName, null);
		service.execute();
	}
	
	
	
	// test 6
	@Test
	public void nullNameAndValue() {
		AddVariableService service = new AddVariableService(token, null, null);
		service.execute();
		
		List<VariableDto> res = service.result();
		assertEquals("Invalid number of variables", 2, res.size());
		
		assertEquals("Invalid existant name or order", existantName1, res.get(0).getName());
		assertEquals("Invalid existant value", existantValue, res.get(0).getValue());
		
		assertEquals("Invalid existant name or order", existantName2, res.get(1).getName());
		assertEquals("Invalid existant value", existantValue, res.get(1).getValue());
	}
	
	
	// test 7
	@Test(expected = InvalidEnvironmentVarNameException.class)
	public void emptyStringName() {
		AddVariableService service = new AddVariableService(token, "", validString);
		service.execute();
	}
	
	
	// test 8
	@Test
	public void emptyStringValue() {
		AddVariableService service = new AddVariableService(token, inexistantName, "");
		service.execute();
		List<VariableDto> res = service.result();
		assertEquals("Invalid number of variables", 3, res.size());

		assertEquals("Invalid existant name or order", existantName1, res.get(0).getName());
		assertEquals("Invalid existant value", existantValue, res.get(0).getValue());
		
		assertEquals("Invalid existant name or order", inexistantName, res.get(1).getName());
		assertEquals("Invalid existant value", "", res.get(1).getValue());
		
		assertEquals("Invalid existant name or order", existantName2, res.get(2).getName());
		assertEquals("Invalid existant value", existantValue, res.get(2).getValue());

	}
	
	// test 9
	@Test
	public void validString() {
		AddVariableService service = new AddVariableService(token, inexistantName, validString);
		service.execute();
		List<VariableDto> res = service.result();
		assertEquals("Invalid number of variables", 3, res.size());
		
		assertEquals("Invalid existant name or order", existantName1, res.get(0).getName());
		assertEquals("Invalid existant value", existantValue, res.get(0).getValue());
		
		assertEquals("Invalid existant name or order", inexistantName, res.get(1).getName());
		assertEquals("Invalid existant value", validString, res.get(1).getValue());
		
		assertEquals("Invalid existant name or order", existantName2, res.get(2).getName());
		assertEquals("Invalid existant value", existantValue, res.get(2).getValue());
		
	}
	
	// test 10
	@Test
	public void existantName() {
		AddVariableService service = new AddVariableService(token, existantName2, validString);
		service.execute();
		List<VariableDto> res = service.result();
		assertEquals("Invalid number of variables", 2, res.size());
		
		assertEquals("Invalid existant name or order", existantName1, res.get(0).getName());
		assertEquals("Invalid existant value", existantValue, res.get(0).getValue());
		
		assertEquals("Invalid existant name or order", existantName2, res.get(1).getName());
		assertEquals("Invalid existant value", validString, res.get(1).getValue());

	}
	
}
