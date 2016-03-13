package pt.tecnico.myDrive.exception;

public class UserDoesNotExistException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String username;

    public UserDoesNotExistException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getMessage() {
        return "The user " + getUsername() + " doesn't exists";
    }
}
