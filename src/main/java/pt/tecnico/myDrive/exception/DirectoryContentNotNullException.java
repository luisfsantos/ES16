package pt.tecnico.myDrive.exception;


public class DirectoryContentNotNullException extends MyDriveException {

	private static final long serialVersionUID = 1L;

	public DirectoryContentNotNullException() {}


	@Override
	public String getMessage() {
		return "Directory contents must be null";
	}
}
