package pt.tecnico.myDrive.exception;

public class FileDoesNotExistInDirectoryException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String dirName;

    public FileDoesNotExistInDirectoryException(String fileName, String dirName) {
        this.fileName = fileName;
        this.dirName = dirName;
    }

    public String getDirName() {
        return dirName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String getMessage() {
        return "\""+getFileName()+"\""+" does not exist in directory "+"\""+getDirName()+"\"";
    }
}

