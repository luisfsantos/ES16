package pt.tecnico.myDrive.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pt.tecnico.myDrive.domain.EnvironmentVariable;
import pt.tecnico.myDrive.exception.InvalidEnvironmentVarNameException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.VariableDto;

public class AddVariableService extends TokenValidationService {
	
 	private String varName;
 	private String value;
 	private List<VariableDto> variables = new ArrayList<>();
 	
 	public AddVariableService(Long token, String varName, String value) {
		super(token);
		this.varName = varName;
		this.value = value;
 	}
 	
 	
 	@Override 
 	protected void dispatch() throws MyDriveException {
		super.dispatch();
		
		if ( varName != null && !session.hasEnvironmentVariable(varName) ) {
			new EnvironmentVariable(session, varName, value); 				// new var
		}
		if ( varName == null && value != null ) {
			throw new InvalidEnvironmentVarNameException(varName);			// exception
		}	
		if (session.hasEnvironmentVariable(varName) && value != null ) {
			session.changeEnvironmentVariable(varName, value);					// set new value
		}

		Set<EnvironmentVariable> environmentVariables = session.getEnvironmentVariableSet();

		for (EnvironmentVariable i : environmentVariables) {
			VariableDto newDto = new VariableDto(i.getName(), i.getValue());
			variables.add(newDto);
		}
		Collections.sort(variables);
 	}
 	
 	public final List<VariableDto> result() {
 		return variables;
 	}
 	
 }