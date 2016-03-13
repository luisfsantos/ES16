package pt.tecnico.myDrive.exception;

public class ImportDocumentException extends MyDriveException {

    private static final long serialVersionUID = 1L;

    public ImportDocumentException() {
        super("Error in importing myDrive from XML");
    }
}
