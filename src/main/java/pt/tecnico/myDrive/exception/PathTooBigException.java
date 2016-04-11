package pt.tecnico.myDrive.exception;

public class PathTooBigException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    public PathTooBigException() {}

    @Override
    public String getMessage() {
        return "Path is too big";
    }
}
