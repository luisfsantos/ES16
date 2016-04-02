package pt.tecnico.myDrive.exception;

public class AccessDeniedToGetTokenException extends MyDriveException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedToGetTokenException(){ }
	
	
	@Override
	public String getMessage() {
		return "Access denied to get token";
	}
}
