package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class DeleteFileService extends TokenValidationService {
	
	private String fileName;
	
	public DeleteFileService(Long token, String fileName) {
		super(token);
		// TODO
	}
	
	
	@Override 
	protected void dispatch() throws MyDriveException {
		
	}
}
