package pt.tecnico.myDrive.exception;

public class AccessDeniedToGetLoginSetException extends MyDriveException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedToGetLoginSetException(){ }
	
	
	@Override
	public String getMessage() {
		return "Access denied to get list of logins";
	}
}
