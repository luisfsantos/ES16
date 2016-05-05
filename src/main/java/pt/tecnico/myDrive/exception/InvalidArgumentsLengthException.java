package pt.tecnico.myDrive.exception;

public class InvalidArgumentsLengthException extends MyDriveException {
    private static final long serialVersionUID = 1L;
    private int argsLength;

    public InvalidArgumentsLengthException(int argsLength) {
        this.argsLength = argsLength;
    }

    @Override
    public String getMessage() {
        return "Invalid arguments length, expected: " + argsLength;
    }
}
