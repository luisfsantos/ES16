package pt.tecnico.myDrive.exception;

public class InvalidFileNameException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String fileName;

    public InvalidFileNameException(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getMessage() {
        return getFileName() + " contains invalid characters";
    }
}