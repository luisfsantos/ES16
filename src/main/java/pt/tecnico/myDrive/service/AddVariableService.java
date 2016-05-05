package pt.tecnico.myDrive.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pt.tecnico.myDrive.domain.EnvironmentVariable;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.VariableDto;

public class AddVariableService extends TokenValidationService {
	
	private Long token;
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

		session.addEnvironmentVariable(varName, value);

		Set<EnvironmentVariable> environmentVariables = session.getEnvironmentVariableSet();

		for (EnvironmentVariable i : environmentVariables) {
			VariableDto newDto = new VariableDto(i.getName(), i.getValue());
			variables.add(newDto);

		}

 	}
 	
 	public final List<VariableDto> result() {
 		return variables;
 	}
 	
 }