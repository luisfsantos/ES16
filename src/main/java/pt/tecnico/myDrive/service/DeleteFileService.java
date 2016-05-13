package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.CannotRemoveDirectoryException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class DeleteFileService extends TokenValidationService {
	
	private String fileName;
	
	public DeleteFileService(Long token, String fname) {
		super(token);
		fileName = fname;
	}
	
	
	@Override 
	protected void dispatch() throws MyDriveException {
		super.dispatch();

		if (fileName.equals(".") || fileName.equals("..")){
			throw new CannotRemoveDirectoryException(fileName);
		}
		
		User user = session.getCurrentUser();
		File f = session.getCurrentDir().lookup(fileName, user);
		f.delete(user);
	}
}
