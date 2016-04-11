package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class CreateFileService extends TokenValidationService {
	
	private Long token;
	private String name; 
	private String type; 
	private String contents;
	
	public CreateFileService(Long token, String name, String type, String contents) {
		super(token);
	}

	@Override
	protected void dispatch() throws MyDriveException {
		// TODO Auto-generated method stub

	}

}
