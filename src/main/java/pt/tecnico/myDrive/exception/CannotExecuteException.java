package pt.tecnico.myDrive.exception;

public class CannotExecuteException extends MyDriveException {
    private String name;

    public CannotExecuteException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return "Can not execute:" + getName();
    }

}
