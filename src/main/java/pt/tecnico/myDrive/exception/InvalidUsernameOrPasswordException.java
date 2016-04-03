package pt.tecnico.myDrive.exception;

public class InvalidUsernameOrPasswordException extends MyDriveException {
	
	private static final long serialVersionUID = 1L;

	public InvalidUsernameOrPasswordException(){ }
	
	
	@Override
	public String getMessage() {
		return "Invalid Username or Password";
	}
}