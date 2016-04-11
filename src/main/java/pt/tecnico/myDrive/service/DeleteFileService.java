package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class DeleteFileService extends MyDriveService {
	
	private Long token;
	private String fileName;
	
	public DeleteFileService(Long token, String fileName) {
		// TODO
	}
	
	
	@Override 
	protected void dispatch() throws MyDriveException {
		
	}
}
