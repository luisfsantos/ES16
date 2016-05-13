package pt.tecnico.myDrive.exception;

/**
 * Created by lads on 10-05-2016.
 */
public class AssociationDoesNotExist extends MyDriveException {
    private String extension;

    public AssociationDoesNotExist(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public String getMessage() {
        return "Default app for ." + getExtension() + " does not exist";
    }
}
