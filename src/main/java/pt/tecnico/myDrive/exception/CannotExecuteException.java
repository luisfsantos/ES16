package pt.tecnico.myDrive.exception;

public class CannotExecuteException extends MyDriveException {
	private static final long serialVersionUID = 1L;
	private String name;

    public CannotExecuteException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return "Cannot execute:" + getName();
    }

}
