package pt.tecnico.myDrive.exception;

public class FileDoesntExistsInDirectoryException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String dirName;

    public FileDoesntExistsInDirectoryException(String fileName, String dirName) {
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
        return "\""+getFileName()+"\""+" doesn't exists in directory "+"\""+getDirName()+"\"";
    }
}

