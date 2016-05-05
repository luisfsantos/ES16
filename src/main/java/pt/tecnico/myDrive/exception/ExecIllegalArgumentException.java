package pt.tecnico.myDrive.exception;

public class ExecIllegalArgumentException extends MyDriveException {
	
	private static final long serialVersionUID = 1L;
	private String cause;

	public ExecIllegalArgumentException(String message){
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
