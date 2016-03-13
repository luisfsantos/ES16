package pt.tecnico.myDrive.exception;

public class WrongSizePermissionException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private int size;

    public WrongSizePermissionException(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String getMessage() {
        return "Expected different size got " + getSize();
    }
}