package pt.tecnico.myDrive.exception;

public class IsNotDirOrLinkExeption extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String fileName;

    public IsNotDirOrLinkExeption(String fileName) {
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

