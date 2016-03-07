package pt.tecnico.myDrive.exception;

public class UserAlreadyExistsException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String username;

    public UserAlreadyExistsException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getMessage() {
        return "User " + username + " already exists";
    }
}
