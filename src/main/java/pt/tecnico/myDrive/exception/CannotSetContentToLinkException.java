package pt.tecnico.myDrive.exception;

/**
 * Created by david on 09-04-2016.
 */
public class CannotSetContentToLinkException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    public CannotSetContentToLinkException(){}

    @Override
    public String getMessage() {
        return "The content of a Link cannot be modified";
    }
}
