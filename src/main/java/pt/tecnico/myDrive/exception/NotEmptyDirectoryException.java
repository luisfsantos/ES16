package pt.tecnico.myDrive.exception;

public class NotEmptyDirectoryException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String dirName;

    public NotEmptyDirectoryException(String dirName) {
        this.dirName = dirName;
    }

    public String getDirName() {
        return dirName;
    }

    @Override
    public String getMessage() {
        return "Directory " + getDirName() + " is not empty";
    }
}