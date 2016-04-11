package pt.tecnico.myDrive.exception;

public class AccessDeniedException extends MyDriveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1659634118485567866L;
	private String deniedOperation;
	private String name;

	public AccessDeniedException(String deniedOp, String name) {
		setDeniedOperation(deniedOp);
		setName(name);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getDeniedOperation() {
		return deniedOperation;
	}

	private void setDeniedOperation(String deniedOperation) {
		this.deniedOperation = deniedOperation;
	}
	
	@Override
	public String getMessage() {
		return "Cannot " + getDeniedOperation() + " " + getName() + ": invalid permissions";
	}

}
