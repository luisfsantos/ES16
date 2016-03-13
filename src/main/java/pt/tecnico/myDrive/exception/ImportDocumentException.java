package pt.tecnico.myDrive.exception;

public class ImportDocumentException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    private String msg;

    public ImportDocumentException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String getMessage() {
        return "Error in importing myDrive from XML: " + getMsg();
    }
}
