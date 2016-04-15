package pt.tecnico.myDrive.exception;

public class IsHomeDirectoryException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String dirName;

    public IsHomeDirectoryException(String dirName) {
        this.dirName = dirName;
    }

    public String getDirName() {
        return dirName;
    }

    @Override
    public String getMessage() {
        return "Directory " + getDirName() + " is a home directory";
    }
}