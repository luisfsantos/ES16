package pt.tecnico.myDrive.service;

import org.joda.time.DateTime;
import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.CannotRemoveDirectoryException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class DeleteFileService extends TokenValidationService {
	
	private String fileName;
	
	public DeleteFileService(Long token, String fname) {
		super(token);
		super.dispatch();
		fileName = fname;
		//session.setLastActivity(new DateTime());
	}
	
	
	@Override 
	protected void dispatch() throws MyDriveException {
		if (fileName.equals(".") || fileName.equals("..")|| fileName.equals("/")){
			throw new CannotRemoveDirectoryException(fileName);
		}
		User user = session.getCurrentUser();
		File f = session.getCurrentDir().lookup(fileName, user);
		f.delete(user);
	}
}
