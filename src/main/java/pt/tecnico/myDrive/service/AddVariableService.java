package pt.tecnico.myDrive.service;

import java.util.List;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.service.dto.VariableDto;

public class AddVariableService extends TokenValidationService {
	
	private Long token;
 	private String varName;
 	private String value;
 	private List<VariableDto> variables;
 	
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