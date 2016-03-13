package pt.tecnico.myDrive.exception;

public class EmptyUsernameException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    public EmptyUsernameException() {
        super("Empty username is not permitted");
    }
}
