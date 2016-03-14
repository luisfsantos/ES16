package pt.tecnico.myDrive.exception;

public class InvalidPermissionException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String permission;

    public InvalidPermissionException(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String getMessage() {
        return getPermission() + " is not a valid Permission";
    }
}
