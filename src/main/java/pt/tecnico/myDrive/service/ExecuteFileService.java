package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.File;
import pt.tecnico.myDrive.exception.InvalidPathException;
import pt.tecnico.myDrive.exception.MyDriveException;

public class ExecuteFileService extends TokenValidationService {

	private String path;
	private String[] args;
	
    public ExecuteFileService(long token, String path, String[] args) {
        super(token);
        this.path = path;
        this.args = args;
    }

    @Override
    protected void dispatch() throws MyDriveException {
    	super.dispatch();
    	File execFile = session.getCurrentDir().lookup(path, session.getCurrentUser());
    	if (execFile.equals(null)) {
    		throw new InvalidPathException(path);
    	}
    	execFile.execute(session.getCurrentUser(), args);
    }
}
