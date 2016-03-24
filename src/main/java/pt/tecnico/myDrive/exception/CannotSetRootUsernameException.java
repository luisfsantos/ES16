package pt.tecnico.myDrive.exception;

public class CannotSetRootUsernameException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    public CannotSetRootUsernameException() {
        super("Cannot set username for SuperUser");
    }
}
