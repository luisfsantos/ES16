package pt.tecnico.myDrive.exception;

public class InvalidUsernameException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String userName;

    public InvalidUsernameException(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String getMessage() {
        return "Username " + getUserName() + " contains invalid characters";
    }
}