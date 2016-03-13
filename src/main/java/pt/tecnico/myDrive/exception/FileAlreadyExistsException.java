package pt.tecnico.myDrive.exception;

public class FileAlreadyExistsException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    private int fileID;

    public FileAlreadyExistsException(int fileID) {
        this.fileID = fileID;
    }

    public int getFileID() {
        return fileID;
    }

    @Override
    public String getMessage() {
        return "File " + getFileID() + " already exists";
    }
}
