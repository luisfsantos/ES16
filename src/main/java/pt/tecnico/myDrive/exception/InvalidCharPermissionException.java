package pt.tecnico.myDrive.exception;

public class InvalidCharPermissionException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private char c;
    private int position;

    public InvalidCharPermissionException(char c, int position) {
        this.c = c;
        this.position = position;
    }

    public char getChar() {
        return c;
    }
    public int getPosition() {
        return position;
    }

    @Override
    public String getMessage() {
        return "Invalid character: " + getChar() + "in position: " + getPosition();
    }
}
