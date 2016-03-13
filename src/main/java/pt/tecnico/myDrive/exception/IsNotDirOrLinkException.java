package pt.tecnico.myDrive.exception;

public class IsNotDirOrLinkException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String fileName;

    public IsNotDirOrLinkException(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getMessage() {
        return getFileName() + " is not a Directory or a Link";
    }
}

