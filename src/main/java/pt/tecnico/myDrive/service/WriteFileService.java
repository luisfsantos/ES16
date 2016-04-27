package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.Login;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.MyDriveException;

public class WriteFileService extends TokenValidationService{


	private String filename;
	private String content;

	public WriteFileService(long token, String filename, String content){
		super(token);
		this.filename = filename;
		this.content = content;
	}

	@Override
	protected void dispatch() throws MyDriveException{
		super.dispatch();
		User user = session.getCurrentUser();

			File f = session.getCurrentDir().lookup(filename,user);
			f.write(user,content);

	}
}
