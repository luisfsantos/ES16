package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.MyDriveException;

public class LogoutService extends TokenValidationService {
	
    public LogoutService(Long token){
    	super(token);
    }

    @Override
    protected void dispatch() throws MyDriveException {
    	super.dispatch();
    	session.remove();
    }
}
