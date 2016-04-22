package pt.tecnico.myDrive.service;

import java.util.List;

import pt.tecnico.myDrive.exception.MyDriveException;

public class AddVariableService extends TokenValidationService {
	
	private Long token;
 	private String varName;
 	private String value;
 	private List<VariableDto> variables;
 	// TODO VariableDto
 	
 	public AddVariableService(Long token, String varName, String value) {
 		super(token);
 	}
 	
 	
 	@Override 
 	protected void dispatch() throws MyDriveException {
 		
 	}
 	
 	public final List<VariableDto> result() {
 		return variables;
 	}
 	
 }