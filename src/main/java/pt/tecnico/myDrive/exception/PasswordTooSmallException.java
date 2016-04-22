package pt.tecnico.myDrive.exception;

/**
 * Created by lads on 22-04-2016.
 */
public class PasswordTooSmallException extends MyDriveException {

	private static final long serialVersionUID = 1L;

	public PasswordTooSmallException() {}

	@Override
	public String getMessage() {
		return "Password is too small";
	}
}
