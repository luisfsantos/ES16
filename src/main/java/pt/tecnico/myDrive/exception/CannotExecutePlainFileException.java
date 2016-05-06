package pt.tecnico.myDrive.exception;

public class CannotExecutePlainFileException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    public CannotExecutePlainFileException() {}

    @Override
    public String getMessage() {
        return "Cannot execute plain file, invalid content";
    }
}
