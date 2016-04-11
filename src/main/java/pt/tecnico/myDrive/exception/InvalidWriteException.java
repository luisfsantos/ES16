package pt.tecnico.myDrive.exception;

/**
 * Created by david on 09-04-2016.
 */
public class InvalidWriteException extends MyDriveException{
    private static final long serialVersionUID = 1L;

    public InvalidWriteException(){}

    @Override
    public String getMessage() {
        return "Can not write in a directory";
    }
}
