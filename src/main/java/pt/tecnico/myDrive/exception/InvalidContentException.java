package pt.tecnico.myDrive.exception;

/**
 * Created by david on 09-04-2016.
 */
public class InvalidContentException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String content;


    public InvalidContentException(String filename, String cont) {

        this.fileName = filename;
        this.content = cont;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getMessage() {
        return getContent() + " is not a valid content to: " + getFileName();
    }
}
