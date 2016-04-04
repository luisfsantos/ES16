package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class CreateFileService extends MyDriveService {
	
	private long token;
	private String name; 
	private String type; 
	private String contents;

	public CreateFileService() {
		// TODO Auto-generated constructor stub
	}
	
	public CreateFileService(long token, String name, String type, String contents) {
		// TODO
	}

	@Override
	protected void dispatch() throws MyDriveException {
		// TODO Auto-generated method stub

	}

}
