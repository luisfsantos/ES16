package pt.tecnico.myDrive.exception;

public class CannotSetRootDirNameException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    public CannotSetRootDirNameException() {
        super("Cannot set name for root directory");
    }
}
