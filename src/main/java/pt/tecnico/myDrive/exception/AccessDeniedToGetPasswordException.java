package pt.tecnico.myDrive.exception;

public class AccessDeniedToGetPasswordException extends MyDriveException {
	
	private static final long serialVersionUID = 1L;

	public AccessDeniedToGetPasswordException(){ }
	
	
	@Override
	public String getMessage() {
		return "Access denied to get user password";
	}
}
