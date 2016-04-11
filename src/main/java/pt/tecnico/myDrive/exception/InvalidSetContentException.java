package pt.tecnico.myDrive.exception;

/**
 * Created by david on 09-04-2016.
 */
public class InvalidSetContentException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    public InvalidSetContentException(){}

    @Override
    public String getMessage() {
        return "The content of a Directory or Link cannot be set";
    }
}
