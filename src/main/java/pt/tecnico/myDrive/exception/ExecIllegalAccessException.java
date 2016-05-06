package pt.tecnico.myDrive.exception;

public class ExecIllegalAccessException extends MyDriveException {
	
	private static final long serialVersionUID = 1L;
	private String cause;

	public ExecIllegalAccessException(String message){
		this.cause = message;
	}
	
	public String getErrorCause () {
		return cause;
	}
	
	@Override
	public String getMessage() {
		return "Cause:" + getErrorCause();
	}
}
