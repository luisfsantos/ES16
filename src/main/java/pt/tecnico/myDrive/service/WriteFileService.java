package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class WriteFileService extends MyDriveService{
	
	private long token;
	private String filename;
	private String content;
	
	public WriteFileService(){
		//TODO
	}
	
	public WriteFileService(long token, String name, String cont){
		
	}

	
	@Override
	protected void dispatch() throws MyDriveException {
		// TODO Auto-generated method stub
		
	}

	
}