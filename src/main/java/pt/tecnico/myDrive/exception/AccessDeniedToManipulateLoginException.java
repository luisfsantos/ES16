package pt.tecnico.myDrive.exception;

public class AccessDeniedToManipulateLoginException extends MyDriveException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedToManipulateLoginException(){ }
	
	
	@Override
	public String getMessage() {
		return "Access denied to manipulate Login";
	}
}
