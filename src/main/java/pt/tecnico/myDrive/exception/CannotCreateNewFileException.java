package pt.tecnico.myDrive.exception;


public class CannotCreateNewFileException extends MyDriveException {
    private static final long serialVersionUID = 1L;

    private String fileType;

    public CannotCreateNewFileException(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }

    @Override
    public String getMessage() {
        return getFileType() + " can not create new files, only the directory can";
    }
}

