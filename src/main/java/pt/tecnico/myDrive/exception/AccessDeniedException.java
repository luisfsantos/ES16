package pt.tecnico.myDrive.exception;

public class AccessDeniedException extends MyDriveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1659634118485567866L;
	private String deniedOperation;
	private String fileName;

	public AccessDeniedException(String deniedOp, String name) {
		setDeniedOperation(deniedOp);
		setFileName(name);
	}

	public String getFileName() {
		return fileName;
	}

	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDeniedOperation() {
		return deniedOperation;
	}

	private void setDeniedOperation(String deniedOperation) {
		this.deniedOperation = deniedOperation;
	}
	
	@Override
	public String getMessage() {
		return "Cannot " + getDeniedOperation() + " " + getFileName() + ": invalid permissions";
	}

}
