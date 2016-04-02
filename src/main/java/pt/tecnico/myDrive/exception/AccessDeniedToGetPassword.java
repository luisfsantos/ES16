package pt.tecnico.myDrive.exception;

public class AccessDeniedToGetPassword extends MyDriveException {
	
	private static final long serialVersionUID = 1L;

	public AccessDeniedToGetPassword(){ }
	
	
	@Override
	public String getMessage() {
		return "Access denied to get user password";
	}
}
